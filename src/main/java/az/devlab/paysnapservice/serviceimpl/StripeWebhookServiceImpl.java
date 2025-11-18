package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.config.StripeProperties;
import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.exception.PaymentException;
import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.PaymentSession;
import az.devlab.paysnapservice.repository.OrderRepository;
import az.devlab.paysnapservice.repository.PaymentSessionRepository;
import az.devlab.paysnapservice.service.ReceiptService;
import az.devlab.paysnapservice.service.StripeWebhookService;
import az.devlab.paysnapservice.util.TimeUtils;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeWebhookServiceImpl implements StripeWebhookService {

    private final StripeProperties stripeProperties;
    private final PaymentSessionRepository paymentSessionRepository;
    private final OrderRepository orderRepository;
    private final ReceiptService receiptService;

    @Override
    public void handleStripeEvent(String payload, String signature) {
        Event event;
        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    stripeProperties.getWebhookSecret()
            );
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Stripe webhook signature");
        }

        String type = event.getType();
        log.info("Stripe webhook received: {}", type);

        switch (type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
            case "checkout.session.expired" -> handleCheckoutSessionExpired(event);
            case "checkout.session.async_payment_failed" -> handleCheckoutSessionFailed(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> log.info("Unhandled Stripe event type: {}", type);
        }

    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (session == null) {
            log.warn("Stripe session is null in completed event");
            return;
        }

        String sessionId = session.getId();
        PaymentSession ps = paymentSessionRepository.findByStripeSessionId(sessionId)
                .orElse(null);

        if (ps == null) {
            log.warn("PaymentSession not found for stripeSessionId={}", sessionId);
            return;
        }

        ps.setStatus(PaymentStatus.COMPLETED);
        ps.setLastStatusUpdateAt(TimeUtils.now());
        paymentSessionRepository.save(ps);

        Order order = ps.getOrder();
        order.setStatus(PaymentStatus.COMPLETED);
        order.setCompletedAt(TimeUtils.now());
        orderRepository.save(order);
        receiptService.generateReceiptForOrder(order.getId());
    }

    private void handleCheckoutSessionExpired(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);
        if (session == null) return;

        String sessionId = session.getId();
        PaymentSession ps = paymentSessionRepository.findByStripeSessionId(sessionId)
                .orElse(null);
        if (ps == null) return;

        ps.setStatus(PaymentStatus.EXPIRED);
        ps.setLastStatusUpdateAt(TimeUtils.now());
        paymentSessionRepository.save(ps);

        Order order = ps.getOrder();
        order.setStatus(PaymentStatus.EXPIRED);
        orderRepository.save(order);
    }

    private void handlePaymentFailed(Event event) {
        log.warn("Stripe payment failed: {}", event.getId());
    }

    private void handleCheckoutSessionFailed(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);
        if (session == null) return;

        String sessionId = session.getId();
        PaymentSession ps = paymentSessionRepository.findByStripeSessionId(sessionId)
                .orElse(null);
        if (ps == null) return;

        ps.setStatus(PaymentStatus.FAILED);
        ps.setLastStatusUpdateAt(TimeUtils.now());
        paymentSessionRepository.save(ps);

        Order order = ps.getOrder();
        order.setStatus(PaymentStatus.FAILED);
        orderRepository.save(order);

        log.warn("Payment failed for Stripe session id={}", sessionId);
    }

}
