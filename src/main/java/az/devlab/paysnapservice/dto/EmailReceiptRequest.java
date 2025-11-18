package az.devlab.paysnapservice.dto;

import lombok.Data;

@Data
public class EmailReceiptRequest {

    private Long orderId;
    private String email;
}
