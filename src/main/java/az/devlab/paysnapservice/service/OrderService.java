package az.devlab.paysnapservice.service;

import az.devlab.paysnapservice.dto.OrderCreateRequest;
import az.devlab.paysnapservice.dto.OrderHistoryItemDto;
import az.devlab.paysnapservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrderForCurrentUser(OrderCreateRequest request);

    OrderResponseDto getOrderForCurrentUser(Long orderId);

    List<OrderHistoryItemDto> getCurrentUserOrderHistory();
}
