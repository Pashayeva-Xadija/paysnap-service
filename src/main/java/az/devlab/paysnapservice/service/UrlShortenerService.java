package az.devlab.paysnapservice.service;

public interface UrlShortenerService {

    String createShortUrlForPayment(String originalUrl);
    String resolveOriginalUrl(String shortCode);
}
