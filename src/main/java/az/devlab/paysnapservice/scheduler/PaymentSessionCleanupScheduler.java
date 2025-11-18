package az.devlab.paysnapservice.scheduler;

import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.model.PaymentSession;
import az.devlab.paysnapservice.repository.PaymentSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSessionCleanupScheduler {

    private final PaymentSessionRepository paymentSessionRepository;

    @Scheduled(fixedDelay = 300000)
    public void cleanExpiredSessions() {

        LocalDateTime now = LocalDateTime.now();

        List<PaymentSession> expiredSessions =
                paymentSessionRepository.findByExpiresAtBefore(now);

        if (expiredSessions.isEmpty()) {
            return;
        }

        expiredSessions.forEach(ps -> {
            ps.setStatus(PaymentStatus.EXPIRED);
            ps.setLastStatusUpdateAt(LocalDateTime.now());
        });

        paymentSessionRepository.saveAll(expiredSessions);

        log.info("Expired payment sessions updated: {}", expiredSessions.size());
    }
}
