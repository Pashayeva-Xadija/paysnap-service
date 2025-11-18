package az.devlab.paysnapservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReceiptResponseDto {

    private Long id;
    private String receiptNumber;

    private Long orderId;
    private Long paymentSessionId;

    private String filePath;
    private String downloadUrl;

    private boolean emailed;
    private String emailedTo;
    private LocalDateTime emailedAt;
    private LocalDateTime createdAt;
}
