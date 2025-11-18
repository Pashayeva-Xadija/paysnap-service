package az.devlab.paysnapservice.service;

public interface QrCodeService {

    byte[] generateQrPngForPaymentSession(Long paymentSessionId);

    byte[] generateQrPdfForPaymentSession(Long paymentSessionId);
}
