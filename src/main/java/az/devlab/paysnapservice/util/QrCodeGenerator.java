package az.devlab.paysnapservice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QrCodeGenerator {


    public static byte[] generatePng(String url, int size) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 1);

            var matrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size, hints);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Failed to generate QR PNG", e);
            throw new RuntimeException("QR code generation failed");
        }
    }

    public static byte[] generatePdf(String url, int size) {
        try {
            byte[] png = generatePng(url, size);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Image img = Image.getInstance(png);
            img.scaleToFit(300, 300);
            img.setAlignment(Image.ALIGN_CENTER);

            document.add(img);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Failed to generate QR PDF", e);
            throw new RuntimeException("QR code PDF creation failed");
        }
    }
}
