package ua.stepess.dnipro.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;
import ua.stepess.dnipro.orderservice.persistence.repository.OrderRepository;
import ua.stepess.dnipro.orderservice.service.client.BookServiceClient;
import ua.stepess.dnipro.orderservice.service.client.UserServiceClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final BookServiceClient bookServiceClient;
    private final UserServiceClient userServiceClient;

    private final OrderRepository orderRepository;

    @Override
    public OrderEntity findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<OrderEntity> findByUserId(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public OrderEntity add(OrderEntity order) {
        order.setPlacedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
