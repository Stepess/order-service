package ua.stepess.dnipro.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.exception.NotEnoughItemsInStockException;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderItem;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderStatus;
import ua.stepess.dnipro.orderservice.persistence.repository.OrderRepository;
import ua.stepess.dnipro.orderservice.service.client.BookServiceClient;
import ua.stepess.dnipro.orderservice.service.client.UserServiceClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
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

    @Override
    public OrderEntity process(OrderEntity orderEntity) {
        var userId = orderEntity.getUserId();

        var user = userServiceClient.getUserById(userId);

        orderEntity.setAddressLine(user.getAddress());

        orderEntity.getOrderDetails()
                .getOrderItems()
                .forEach(this::checkItemAvailabilityInStock);

        orderEntity.setPlacedAt(LocalDateTime.now());
        orderEntity.setStatus(OrderStatus.VERIFIED);

        return add(orderEntity);
    }

    private void checkItemAvailabilityInStock(OrderItem orderItem) {
        var itemFromStock = bookServiceClient.getItemById(orderItem.getItemId());
        if (itemFromStock.getQuantity() < orderItem.getQuantity()) {
            log.warn("Can't perform order placing: requested [{}], available [{}]",
                    orderItem.getQuantity(), itemFromStock.getQuantity());
            throw new NotEnoughItemsInStockException();
        }
    }
}
