package ua.stepess.dnipro.orderservice.persistence.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetails {
    private List<OrderItem> orderItems;
}
