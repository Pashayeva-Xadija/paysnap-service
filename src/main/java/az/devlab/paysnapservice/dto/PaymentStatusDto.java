package az.devlab.paysnapservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentStatusDto {

    private Long paymentSessionId;
    private Long orderId;
    private String status;
    private LocalDateTime lastUpdatedAt;
}
