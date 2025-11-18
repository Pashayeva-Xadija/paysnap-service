package az.devlab.paysnapservice.service;

public interface StripeWebhookService {
    void handleStripeEvent(String payload, String signature);
}
