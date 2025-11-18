package az.devlab.paysnapservice.service;

import az.devlab.paysnapservice.dto.EmailReceiptRequest;
import az.devlab.paysnapservice.dto.ReceiptResponseDto;

public interface ReceiptService {

    ReceiptResponseDto generateReceiptForOrder(Long orderId);

    ReceiptResponseDto getLatestReceiptForOrder(Long orderId);

    ReceiptResponseDto getReceiptById(Long receiptId);

    void sendReceiptByEmail(EmailReceiptRequest request);
}
