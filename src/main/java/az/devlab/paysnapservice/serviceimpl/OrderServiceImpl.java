package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.dto.OrderCreateRequest;
import az.devlab.paysnapservice.dto.OrderHistoryItemDto;
import az.devlab.paysnapservice.dto.OrderResponseDto;
import az.devlab.paysnapservice.enums.PaymentStatus;
import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.mapper.OrderMapper;
import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.User;
import az.devlab.paysnapservice.repository.OrderRepository;
import az.devlab.paysnapservice.repository.ReceiptRepository;
import az.devlab.paysnapservice.repository.UserRepository;
import az.devlab.paysnapservice.service.OrderService;
import az.devlab.paysnapservice.service.ReceiptService;
import az.devlab.paysnapservice.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ReceiptService receiptService;
    private final ReceiptRepository receiptRepository;


    @Override
    public OrderResponseDto createOrderForCurrentUser(OrderCreateRequest request) {
        User user = getCurrentUser();
        Order order = orderMapper.toEntity(request);
        order.setUser(user);
        order.setStatus(PaymentStatus.PENDING);
        order.setCreatedAt(TimeUtils.now());

        order = orderRepository.save(order);

        OrderResponseDto dto = orderMapper.toDto(order);
        return dto;
    }

    @Override
    public OrderResponseDto getOrderForCurrentUser(Long orderId) {
        User user = getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Order not found for current user"));

        OrderResponseDto dto = orderMapper.toDto(order);

        return dto;
    }

    @Override
    public List<OrderHistoryItemDto> getCurrentUserOrderHistory() {
        User user = getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderHistoryItemDto> dtos = orderMapper.toHistoryItemDtoList(orders);

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);

            var receipts = receiptRepository.findByOrderId(order.getId());
            if (!receipts.isEmpty()) {
                var latest = receipts.get(receipts.size() - 1);
                String downloadUrl = "/api/receipts/" + latest.getId() + "/download";
                dtos.get(i).setReceiptDownloadUrl(downloadUrl);
            }
        }

        return dtos;
    }


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }
}
