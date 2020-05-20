package ua.stepess.dnipro.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.stepess.dnipro.orderservice.persistence.entity.Order;

import java.util.UUID;

public interface OrderService {

    Order findById(UUID id);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUserId(String userId, Pageable pageable);

    Order add(Order order);

}
