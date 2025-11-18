package az.devlab.paysnapservice.mapper;

import az.devlab.paysnapservice.dto.OrderCreateRequest;
import az.devlab.paysnapservice.dto.OrderHistoryItemDto;
import az.devlab.paysnapservice.dto.OrderResponseDto;
import az.devlab.paysnapservice.enums.CurrencyCode;
import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "completedAt", ignore = true),
            @Mapping(target = "paymentSession", ignore = true),
            @Mapping(target = "receipts", ignore = true),
            @Mapping(target = "currency", expression = "java(mapCurrency(orderCreateRequest.getCurrency()))")
    })
    Order toEntity(OrderCreateRequest orderCreateRequest);

    @Mappings({
            @Mapping(target = "currency", expression = "java(mapCurrencyToString(order.getCurrency()))"),
            @Mapping(target = "status", expression = "java(mapStatusToString(order.getStatus()))"),
            @Mapping(target = "paymentUrl", ignore = true),
            @Mapping(target = "shortPaymentUrl", ignore = true),
            @Mapping(target = "qrCodeUrl", ignore = true),
            @Mapping(target = "receiptDownloadUrl", ignore = true)
    })
    OrderResponseDto toDto(Order order);

    @Mappings({
            @Mapping(target = "orderId", source = "id"),
            @Mapping(target = "currency", expression = "java(mapCurrencyToString(order.getCurrency()))"),
            @Mapping(target = "status", expression = "java(mapStatusToString(order.getStatus()))"),
            @Mapping(target = "receiptDownloadUrl", ignore = true)
    })
    OrderHistoryItemDto toHistoryItemDto(Order order);

    List<OrderHistoryItemDto> toHistoryItemDtoList(List<Order> orders);


    default CurrencyCode mapCurrency(String code) {
        if (code == null) {
            return null;
        }
        return CurrencyCode.valueOf(code.toUpperCase());
    }

    default String mapCurrencyToString(CurrencyCode currency) {
        return currency != null ? currency.name() : null;
    }

    default String mapStatusToString(PaymentStatus status) {
        return status != null ? status.name() : null;
    }
}
