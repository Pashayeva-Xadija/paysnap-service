package az.devlab.paysnapservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_session_id")
    private PaymentSession paymentSession;

    @Column(nullable = false, unique = true, length = 100)
    private String receiptNumber;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(length = 100)
    private String contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean emailed;

    @Column(length = 200)
    private String emailedTo;

    private LocalDateTime emailedAt;
}
