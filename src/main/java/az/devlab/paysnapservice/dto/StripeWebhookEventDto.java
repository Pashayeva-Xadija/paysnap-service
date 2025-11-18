package az.devlab.paysnapservice.dto;

import lombok.Data;

@Data
public class StripeWebhookEventDto {

    private String eventId;
    private String eventType;
    private String stripeSessionId;
    private String rawPayload;
    private String signature;
}
