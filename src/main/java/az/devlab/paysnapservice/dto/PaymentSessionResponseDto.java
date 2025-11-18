package az.devlab.paysnapservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentSessionResponseDto {

    private Long paymentSessionId;
    private String stripeSessionId;

    private String paymentUrl;
    private String shortPaymentUrl;
    private String qrCodeUrl;

    private String status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
