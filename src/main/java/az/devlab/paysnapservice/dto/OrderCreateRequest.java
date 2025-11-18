package az.devlab.paysnapservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {

    private BigDecimal amount;
    private String currency;
    private String description;
}
