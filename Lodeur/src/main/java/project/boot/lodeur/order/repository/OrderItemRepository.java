package project.boot.lodeur.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.lodeur.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
