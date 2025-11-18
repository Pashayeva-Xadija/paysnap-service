package az.devlab.paysnapservice.controller;

import az.devlab.paysnapservice.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @GetMapping("/{paymentSessionId}/png")
    public ResponseEntity<byte[]> getQrPng(@PathVariable Long paymentSessionId) {
        byte[] bytes = qrCodeService.generateQrPngForPaymentSession(paymentSessionId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }

    @GetMapping("/{paymentSessionId}/pdf")
    public ResponseEntity<byte[]> getQrPdf(@PathVariable Long paymentSessionId) {
        byte[] bytes = qrCodeService.generateQrPdfForPaymentSession(paymentSessionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qr-" + paymentSessionId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
