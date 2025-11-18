package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.dto.EmailReceiptRequest;
import az.devlab.paysnapservice.dto.ReceiptResponseDto;
import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.exception.PdfGenerationException;
import az.devlab.paysnapservice.mapper.ReceiptMapper;
import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.Receipt;
import az.devlab.paysnapservice.model.User;
import az.devlab.paysnapservice.repository.OrderRepository;
import az.devlab.paysnapservice.repository.ReceiptRepository;
import az.devlab.paysnapservice.service.ReceiptService;
import az.devlab.paysnapservice.util.FileUtils;
import az.devlab.paysnapservice.util.PdfGenerator;
import az.devlab.paysnapservice.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final OrderRepository orderRepository;
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final JavaMailSender mailSender;

    @Override
    public ReceiptResponseDto generateReceiptForOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = order.getUser();

        byte[] pdfBytes = PdfGenerator.generateReceiptPdf(order, user);

        String receiptNumber = UUID.randomUUID().toString();
        Path path = FileUtils.receiptPath(orderId, receiptNumber);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, pdfBytes);
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to store receipt PDF file");
        }

        Receipt receipt = Receipt.builder()
                .order(order)
                .paymentSession(order.getPaymentSession())
                .receiptNumber(receiptNumber)
                .filePath(path.toString())
                .contentType("application/pdf")
                .createdAt(TimeUtils.now())
                .emailed(false)
                .build();

        receipt = receiptRepository.save(receipt);

        ReceiptResponseDto dto = receiptMapper.toDto(receipt);
        dto.setDownloadUrl("/api/receipts/" + receipt.getId() + "/download");
        return dto;
    }

    @Override
    public ReceiptResponseDto getLatestReceiptForOrder(Long orderId) {
        var receipts = receiptRepository.findByOrderId(orderId);
        if (receipts.isEmpty()) {
            throw new NotFoundException("No receipt found for order id: " + orderId);
        }
        Receipt latest = receipts.get(receipts.size() - 1);
        ReceiptResponseDto dto = receiptMapper.toDto(latest);
        dto.setDownloadUrl("/api/receipts/" + latest.getId() + "/download");
        return dto;
    }

    @Override
    public ReceiptResponseDto getReceiptById(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException("Receipt not found with id: " + receiptId));
        ReceiptResponseDto dto = receiptMapper.toDto(receipt);
        dto.setDownloadUrl("/api/receipts/" + receipt.getId() + "/download");
        return dto;
    }

    @Override
    public void sendReceiptByEmail(EmailReceiptRequest request) {
        ReceiptResponseDto receiptDto = getLatestReceiptForOrder(request.getOrderId());
        Receipt receipt = receiptRepository.findById(receiptDto.getId())
                .orElseThrow(() -> new NotFoundException("Receipt not found"));

        try {
            Path path = Path.of(receipt.getFilePath());
            byte[] bytes = Files.readAllBytes(path);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getEmail());
            helper.setSubject("Payment receipt for order #" + receipt.getOrder().getId());
            helper.setText("Please find your payment receipt attached.", false);
            helper.addAttachment("receipt-" + receipt.getReceiptNumber() + ".pdf",
                    () -> Files.newInputStream(path),
                    "application/pdf");

            mailSender.send(message);

            receipt.setEmailed(true);
            receipt.setEmailedTo(request.getEmail());
            receipt.setEmailedAt(LocalDateTime.now());
            receiptRepository.save(receipt);

        } catch (Exception e) {
            log.error("Failed to send receipt email", e);
            throw new PdfGenerationException("Failed to send receipt email");
        }
    }
}
