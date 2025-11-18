package az.devlab.paysnapservice.controller;

import az.devlab.paysnapservice.dto.PaymentSessionResponseDto;
import az.devlab.paysnapservice.dto.PaymentStatusDto;
import az.devlab.paysnapservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/session")
    public ResponseEntity<PaymentSessionResponseDto> createPaymentSession(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.createPaymentSessionForOrder(orderId));
    }

    @GetMapping("/sessions/{paymentSessionId}/status")
    public ResponseEntity<PaymentStatusDto> getStatusBySessionId(@PathVariable Long paymentSessionId) {
        return ResponseEntity.ok(paymentService.getPaymentStatusBySessionId(paymentSessionId));
    }

    @GetMapping("/sessions/stripe/{stripeSessionId}/status")
    public ResponseEntity<PaymentStatusDto> getStatusByStripeSessionId(@PathVariable String stripeSessionId) {
        return ResponseEntity.ok(paymentService.getPaymentStatusByStripeSessionId(stripeSessionId));
    }
}
