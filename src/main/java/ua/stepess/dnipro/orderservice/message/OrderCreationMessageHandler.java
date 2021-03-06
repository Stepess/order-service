package ua.stepess.dnipro.orderservice.message;

import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import ua.stepess.dnipro.orderservice.exception.OrderCreationMessageProcessingFailed;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderDetails;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderItem;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderStatus;
import ua.stepess.dnipro.orderservice.service.OrderService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class OrderCreationMessageHandler implements MessageHandler {

    private final OrderService orderService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        var payload = (byte[]) message.getPayload();
        log.info("Received order creation event: message id [{}]", message.getHeaders().getId());

        try {
            var orderDto = parsePayload(payload);

            log.info("Parsed order DTO: [{}]", orderDto);

            var orderEntity = mapOrderDtoToEntity(orderDto);

            orderService.process(orderEntity);
        } catch (Exception ex) {
            log.error("Failed to precess message", ex);
            toBasicAcknowledgeablePubsubMessage(message)
                    .ifPresent(BasicAcknowledgeablePubsubMessage::nack);
            throw new OrderCreationMessageProcessingFailed("Message processing failed", ex);
        }

        log.info("Message processed: message id [{}]", message.getHeaders().getId());
        toBasicAcknowledgeablePubsubMessage(message)
                .ifPresent(BasicAcknowledgeablePubsubMessage::ack);
    }

    private Optional<BasicAcknowledgeablePubsubMessage> toBasicAcknowledgeablePubsubMessage(Message<?> message) {
        return Optional.of(message.getHeaders())
                .map(headers -> headers.get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                        BasicAcknowledgeablePubsubMessage.class));
    }

    private OrderEntity mapOrderDtoToEntity(Order orderDto) {
        var orderEntity = new OrderEntity();

        orderEntity.setUserId(String.valueOf(orderDto.getUserId()));
        orderEntity.setStatus(OrderStatus.PENDING);

        var orderItems = orderDto.getEntries().stream()
                .map(this::mapOrderEntryToOrderItem)
                .collect(Collectors.toList());

        var orderDetails = new OrderDetails();
        orderDetails.setOrderItems(orderItems);

        orderEntity.setOrderDetails(orderDetails);

        return orderEntity;
    }

    private OrderItem mapOrderEntryToOrderItem(OrderEntry entry) {
        var orderItem = new OrderItem();
        orderItem.setItemId(entry.getItemId());
        orderItem.setQuantity((long) entry.getNumber());
        return orderItem;
    }

    private Order parsePayload(byte[] payload) {
        try {
            return Order.fromByteBuffer(
                    ByteBuffer.wrap(payload));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read message", e);
        }
    }

}
