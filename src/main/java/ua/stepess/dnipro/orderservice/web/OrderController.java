package ua.stepess.dnipro.orderservice.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ua.stepess.dnipro.orderservice.persistence.entity.Order;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return null;
    }

    @GetMapping("/{orderId}")
    public Order getById(@PathVariable String orderId) {
        return null;
    }

    @GetMapping(params = "userId")
    public List<Order> getByUserId(@RequestParam String userId, Pageable pageable) {
        return null;
    }

}
