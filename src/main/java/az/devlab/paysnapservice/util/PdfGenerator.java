package az.devlab.paysnapservice.util;

import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.User;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
public class
PdfGenerator {

    public static byte[] generateReceiptPdf(Order order, User user) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            document.add(new Paragraph("PAYMENT RECEIPT"));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Customer: " + user.getFullName()));
            document.add(new Paragraph("Amount: " + order.getAmount() + " " + order.getCurrency().name()));
            document.add(new Paragraph("Status: " + order.getStatus().name()));
            document.add(new Paragraph("Created At: " + order.getCreatedAt()));
            if (order.getCompletedAt() != null) {
                document.add(new Paragraph("Completed At: " + order.getCompletedAt()));
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("PDF receipt generation failed", e);
            throw new RuntimeException("PDF generation error");
        }
    }
}
