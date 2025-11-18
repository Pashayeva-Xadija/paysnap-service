package az.devlab.paysnapservice.mapper;

import az.devlab.paysnapservice.dto.PaymentSessionResponseDto;
import az.devlab.paysnapservice.dto.PaymentStatusDto;
import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.model.PaymentSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentSessionMapper {

    @Mappings({
            @Mapping(target = "paymentSessionId", source = "id"),
            @Mapping(target = "status", expression = "java(mapStatusToString(paymentSession.getStatus()))"),
            @Mapping(target = "shortPaymentUrl", ignore = true),
            @Mapping(target = "qrCodeUrl", ignore = true)
    })
    PaymentSessionResponseDto toDto(PaymentSession paymentSession);

    List<PaymentSessionResponseDto> toDtoList(List<PaymentSession> sessions);

    @Mappings({
            @Mapping(target = "paymentSessionId", source = "id"),
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "status", expression = "java(mapStatusToString(paymentSession.getStatus()))"),
            @Mapping(target = "lastUpdatedAt", source = "lastStatusUpdateAt")
    })
    PaymentStatusDto toStatusDto(PaymentSession paymentSession);

    default String mapStatusToString(PaymentStatus status) {
        return status != null ? status.name() : null;
    }
}
