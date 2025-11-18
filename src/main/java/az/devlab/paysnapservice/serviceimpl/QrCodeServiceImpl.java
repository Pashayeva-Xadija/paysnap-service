package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.model.PaymentSession;
import az.devlab.paysnapservice.repository.PaymentSessionRepository;
import az.devlab.paysnapservice.service.QrCodeService;
import az.devlab.paysnapservice.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private final PaymentSessionRepository paymentSessionRepository;

    private static final int QR_SIZE = 300;

    @Override
    public byte[] generateQrPngForPaymentSession(Long paymentSessionId) {
        PaymentSession session = paymentSessionRepository.findById(paymentSessionId)
                .orElseThrow(() -> new NotFoundException("Payment session not found with id: " + paymentSessionId));

        return QrCodeGenerator.generatePng(session.getPaymentUrl(), QR_SIZE);
    }

    @Override
    public byte[] generateQrPdfForPaymentSession(Long paymentSessionId) {
        PaymentSession session = paymentSessionRepository.findById(paymentSessionId)
                .orElseThrow(() -> new NotFoundException("Payment session not found with id: " + paymentSessionId));

        return QrCodeGenerator.generatePdf(session.getPaymentUrl(), QR_SIZE);
    }
}
