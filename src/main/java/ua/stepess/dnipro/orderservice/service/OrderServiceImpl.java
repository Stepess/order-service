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
        log.info("Retrieving order by id [{}]", id);
        return orderRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable) {
        log.info("Retrieving all orders");
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<OrderEntity> findByUserId(String userId, Pageable pageable) {
        log.info("Retrieving order by user id [{}]", userId);
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public OrderEntity add(OrderEntity order) {
        order.setPlacedAt(LocalDateTime.now());
        log.info("Saving order: [{}]", order);
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity process(OrderEntity orderEntity) {
        log.info("Processing order entity: [{}]", orderEntity);
        var userId = orderEntity.getUserId();

        var user = userServiceClient.getUserById(userId);

        orderEntity.setAddressLine(user.getAddress());

        orderEntity.getOrderDetails()
                .getOrderItems()
                .parallelStream()
                .forEach(this::checkItemAvailabilityInStock);

        orderEntity.setStatus(OrderStatus.VERIFIED);

        log.info("Order for user id [{}] processed successfully", orderEntity.getUserId());
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
