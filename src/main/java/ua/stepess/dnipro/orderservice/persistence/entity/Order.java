package ua.stepess.dnipro.orderservice.persistence.entity;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private String userId;
    private String addressLine;
    private OrderStatus status;
    private OrderDetails orderDetails;
}
