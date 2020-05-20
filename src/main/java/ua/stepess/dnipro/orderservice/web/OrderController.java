package ua.stepess.dnipro.orderservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.stepess.dnipro.orderservice.persistence.entity.Order;
import ua.stepess.dnipro.orderservice.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{orderId}")
    public Order getById(@PathVariable UUID orderId) {
        return orderService.findById(orderId);
    }

    @GetMapping(params = "userId")
    public Page<Order> getByUserId(@RequestParam String userId, Pageable pageable) {
        return orderService.findByUserId(userId, pageable);
    }

    @PostMapping
    public Order save(@RequestBody Order order) {
        return orderService.add(order);
    }

}
