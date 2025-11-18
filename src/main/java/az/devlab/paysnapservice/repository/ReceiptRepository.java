package az.devlab.paysnapservice.repository;

import az.devlab.paysnapservice.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findByOrderId(Long orderId);

    boolean existsByReceiptNumber(String receiptNumber);
}
