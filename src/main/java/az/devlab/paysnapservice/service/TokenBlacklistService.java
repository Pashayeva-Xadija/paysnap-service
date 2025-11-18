package az.devlab.paysnapservice.service;

import java.time.Instant;

public interface TokenBlacklistService {

    void blacklistToken(String token, Instant expiresAt);
    boolean isTokenBlacklisted(String token);
}
