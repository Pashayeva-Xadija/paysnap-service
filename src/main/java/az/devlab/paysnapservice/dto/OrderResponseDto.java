package az.devlab.paysnapservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDto {

    private Long id;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    private String paymentUrl;
    private String shortPaymentUrl;
    private String qrCodeUrl;

    private String receiptDownloadUrl;
}
