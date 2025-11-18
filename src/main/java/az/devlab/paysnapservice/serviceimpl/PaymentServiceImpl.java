package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.config.StripeProperties;
import az.devlab.paysnapservice.dto.PaymentSessionResponseDto;
import az.devlab.paysnapservice.dto.PaymentStatusDto;
import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.exception.PaymentException;
import az.devlab.paysnapservice.mapper.PaymentSessionMapper;
import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.PaymentSession;
import az.devlab.paysnapservice.repository.OrderRepository;
import az.devlab.paysnapservice.repository.PaymentSessionRepository;
import az.devlab.paysnapservice.service.PaymentService;
import az.devlab.paysnapservice.service.UrlShortenerService;
import az.devlab.paysnapservice.util.TimeUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentSessionRepository paymentSessionRepository;
    private final PaymentSessionMapper paymentSessionMapper;
    private final UrlShortenerService urlShortenerService;
    private final StripeProperties stripeProperties;

    private static final int PAYMENT_LINK_EXPIRY_MINUTES = 15;

    @Override
    public PaymentSessionResponseDto createPaymentSessionForOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(stripeProperties.getSuccessUrl())
                    .setCancelUrl(stripeProperties.getCancelUrl())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(order.getCurrency().name().toLowerCase())
                                                    .setUnitAmount(order.getAmount().multiply(java.math.BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order #" + order.getId())
                                                                    .setDescription(order.getDescription())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            PaymentSession paymentSession = PaymentSession.builder()
                    .order(order)
                    .stripeSessionId(session.getId())
                    .paymentUrl(session.getUrl())
                    .status(PaymentStatus.PENDING)
                    .createdAt(TimeUtils.now())
                    .expiresAt(TimeUtils.minutesFromNow(PAYMENT_LINK_EXPIRY_MINUTES))
                    .build();

            String shortUrl = urlShortenerService.createShortUrlForPayment(paymentSession.getPaymentUrl());
            paymentSessionRepository.save(paymentSession);

            PaymentSessionResponseDto dto = paymentSessionMapper.toDto(paymentSession);
            dto.setShortPaymentUrl(shortUrl);
            return dto;

        } catch (StripeException e) {
            throw new PaymentException("Failed to create Stripe session: " + e.getMessage());
        }
    }

    @Override
    public PaymentStatusDto getPaymentStatusBySessionId(Long paymentSessionId) {
        PaymentSession session = paymentSessionRepository.findById(paymentSessionId)
                .orElseThrow(() -> new NotFoundException("Payment session not found with id: " + paymentSessionId));

        return paymentSessionMapper.toStatusDto(session);
    }

    @Override
    public PaymentStatusDto getPaymentStatusByStripeSessionId(String stripeSessionId) {
        PaymentSession session = paymentSessionRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(() -> new NotFoundException("Payment session not found for stripe session id: " + stripeSessionId));

        return paymentSessionMapper.toStatusDto(session);
    }
}
