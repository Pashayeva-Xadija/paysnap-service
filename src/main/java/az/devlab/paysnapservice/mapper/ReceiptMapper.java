package az.devlab.paysnapservice.mapper;

import az.devlab.paysnapservice.dto.ReceiptResponseDto;
import az.devlab.paysnapservice.model.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mappings({
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "paymentSessionId", source = "paymentSession.id"),
            @Mapping(target = "downloadUrl", ignore = true)
    })
    ReceiptResponseDto toDto(Receipt receipt);

    List<ReceiptResponseDto> toDtoList(List<Receipt> receipts);
}
