package ua.stepess.dnipro.orderservice.persistence.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class OrderEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Type(type = "jsonb")
    @Column(name = "order_details", nullable = false, columnDefinition = "jsonb")
    private OrderDetails orderDetails;

    @Column(name = "placed_at", nullable = false)
    private LocalDateTime placedAt;
}
