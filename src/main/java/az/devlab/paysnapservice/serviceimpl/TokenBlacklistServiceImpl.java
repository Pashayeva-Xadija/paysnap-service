package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.model.BlacklistedToken;
import az.devlab.paysnapservice.repository.BlacklistedTokenRepository;
import az.devlab.paysnapservice.service.TokenBlacklistService;
import az.devlab.paysnapservice.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final BlacklistedTokenRepository tokenRepository;

    @Override
    public void blacklistToken(String token, Instant expiresAt) {
        String tokenHash = TokenUtils.hashToken(token);

        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .id(tokenHash)
                .token(tokenHash)
                .expiresAt(expiresAt)
                .build();

        tokenRepository.save(blacklistedToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        String tokenHash = TokenUtils.hashToken(token);

        return tokenRepository.findById(tokenHash)
                .map(saved -> {
                    if (saved.getExpiresAt().isBefore(Instant.now())) {
                        tokenRepository.deleteById(tokenHash);
                        return false;
                    }
                    return true;
                })
                .orElse(false);
    }
}
