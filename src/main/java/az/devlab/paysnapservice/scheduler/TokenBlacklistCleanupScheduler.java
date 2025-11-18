package az.devlab.paysnapservice.scheduler;

import az.devlab.paysnapservice.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlacklistCleanupScheduler {

    private final BlacklistedTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {

        Iterable<az.devlab.paysnapservice.model.BlacklistedToken> tokens = tokenRepository.findAll();
        Instant now = Instant.now();
        int removed = 0;

        for (var token : tokens) {
            if (token.getExpiresAt().isBefore(now)) {
                tokenRepository.deleteById(token.getId());
                removed++;
            }
        }

        if (removed > 0) {
            log.info("Cleaned {} expired blacklisted tokens", removed);
        }
    }
}
