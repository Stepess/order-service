package ua.stepess.dnipro.orderservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import ua.stepess.dnipro.orderservice.exception.OrderCreationMessageProcessingFailed;
import ua.stepess.dnipro.orderservice.generated.avro.Order;
import ua.stepess.dnipro.orderservice.generated.avro.OrderEntry;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderDetails;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderItem;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderStatus;
import ua.stepess.dnipro.orderservice.service.OrderService;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class OrderCreationMessageHandler implements MessageHandler {

    private final ObjectMapper mapper;
    private final OrderService orderService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        var payload = new String((byte[]) message.getPayload());
        log.info("Received order creation event: [{}]", payload);

        try {
            var orderDto = parsePayload(payload);

            var orderEntity = mapOrderDtoToEntity(orderDto);

            orderService.process(orderEntity);
        } catch (Exception ex) {
            log.error("Failed to precess message");
            toBasicAcknowledgeablePubsubMessage(message)
                    .ifPresent(BasicAcknowledgeablePubsubMessage::nack);
            throw new OrderCreationMessageProcessingFailed("Message failed", ex);
        }

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
        orderItem.setItemId(String.valueOf(entry.getItemId()));
        orderItem.setQuantity((long) entry.getNumber());
        return orderItem;
    }

    private Order parsePayload(String payload) {
        try {
            return mapper.readValue(payload, Order.class);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException("Failed to read message", e);
        }
    }

}
