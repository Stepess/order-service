package ua.stepess.dnipro.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;

import java.util.UUID;

public interface OrderService {

    OrderEntity findById(UUID id);

    Page<OrderEntity> findAll(Pageable pageable);

    Page<OrderEntity> findByUserId(String userId, Pageable pageable);

    OrderEntity add(OrderEntity order);

}
