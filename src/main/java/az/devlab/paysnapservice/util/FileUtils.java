package az.devlab.paysnapservice.util;

import java.nio.file.Path;

public class FileUtils {

    public static Path receiptPath(Long orderId, String receiptNumber) {
        return Path.of("storage/receipts/order-" + orderId + "-" + receiptNumber + ".pdf");
    }

    public static Path qrPngPath(Long paymentSessionId) {
        return Path.of("storage/qrcodes/qr-" + paymentSessionId + ".png");
    }

    public static Path qrPdfPath(Long paymentSessionId) {
        return Path.of("storage/qrcodes/qr-" + paymentSessionId + ".pdf");
    }
}
