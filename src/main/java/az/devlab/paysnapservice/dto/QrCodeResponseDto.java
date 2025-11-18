package az.devlab.paysnapservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrCodeResponseDto {

    private String qrCodeUrlPng;
    private String qrCodeUrlPdf;
}
