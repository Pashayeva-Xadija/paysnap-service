package az.devlab.paysnapservice.controller;

import az.devlab.paysnapservice.dto.EmailReceiptRequest;
import az.devlab.paysnapservice.dto.ReceiptResponseDto;
import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.model.Receipt;
import az.devlab.paysnapservice.repository.ReceiptRepository;
import az.devlab.paysnapservice.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final ReceiptRepository receiptRepository;

    @PostMapping("/generate/{orderId}")
    public ResponseEntity<ReceiptResponseDto> generateReceipt(@PathVariable Long orderId) {
        return ResponseEntity.ok(receiptService.generateReceiptForOrder(orderId));
    }

    @GetMapping("/order/{orderId}/latest")
    public ResponseEntity<ReceiptResponseDto> getLatestReceipt(@PathVariable Long orderId) {
        return ResponseEntity.ok(receiptService.getLatestReceiptForOrder(orderId));
    }

    @GetMapping("/{receiptId}/download")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long receiptId) throws Exception {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException("Receipt not found with id: " + receiptId));

        Path path = Path.of(receipt.getFilePath());
        byte[] bytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=receipt-" + receipt.getReceiptNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @PostMapping("/email")
    public ResponseEntity<Void> emailReceipt(@Valid @RequestBody EmailReceiptRequest request) {
        receiptService.sendReceiptByEmail(request);
        return ResponseEntity.noContent().build();
    }
}
