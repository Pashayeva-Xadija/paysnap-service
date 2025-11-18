package az.devlab.paysnapservice.repository;

import az.devlab.paysnapservice.model.Order;
import az.devlab.paysnapservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByUserId(Long userId);
}
