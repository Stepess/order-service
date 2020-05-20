package ua.stepess.dnipro.orderservice.persistence.entity;

import lombok.Data;

@Data
public class OrderItem {
    private String itemId;
    private Long quantity;
}
