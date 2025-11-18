package az.devlab.paysnapservice.security;

import az.devlab.paysnapservice.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenBlacklistChecker {

    private final TokenBlacklistService tokenBlacklistService;


    public boolean isBlacklisted(String token) {
        return tokenBlacklistService.isTokenBlacklisted(token);
    }
}
