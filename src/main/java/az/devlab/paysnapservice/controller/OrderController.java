package az.devlab.paysnapservice.controller;

import az.devlab.paysnapservice.dto.OrderCreateRequest;
import az.devlab.paysnapservice.dto.OrderHistoryItemDto;
import az.devlab.paysnapservice.dto.OrderResponseDto;
import az.devlab.paysnapservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.createOrderForCurrentUser(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderForCurrentUser(orderId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryItemDto>> getOrderHistory() {
        return ResponseEntity.ok(orderService.getCurrentUserOrderHistory());
    }
}
