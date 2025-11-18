package az.devlab.paysnapservice.repository;

import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.model.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface PaymentSessionRepository extends JpaRepository<PaymentSession, Long> {

    Optional<PaymentSession> findByStripeSessionId(String stripeSessionId);

    List<PaymentSession> findByStatus(PaymentStatus status);

    List<PaymentSession> findByExpiresAtBefore(LocalDateTime time);
}
