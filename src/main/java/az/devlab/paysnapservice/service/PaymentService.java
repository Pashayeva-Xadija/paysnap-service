package az.devlab.paysnapservice.service;

import az.devlab.paysnapservice.dto.PaymentSessionResponseDto;
import az.devlab.paysnapservice.dto.PaymentStatusDto;

public interface PaymentService {
    PaymentSessionResponseDto createPaymentSessionForOrder(Long orderId);

    PaymentStatusDto getPaymentStatusBySessionId(Long paymentSessionId);

    PaymentStatusDto getPaymentStatusByStripeSessionId(String stripeSessionId);
}
