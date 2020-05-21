package ua.stepess.dnipro.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.exception.OrderCreationMessageProcessingFailed;
import ua.stepess.dnipro.orderservice.generated.avro.Order;
import ua.stepess.dnipro.orderservice.generated.avro.OrderEntry;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderDetails;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderItem;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderStatus;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreationMessageHandler implements MessageHandler {

    private final OrderService orderService;
    private final ObjectMapper mapper;

    @Override
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void handleMessage(Message<?> message) throws MessagingException {
        var payload = new String((byte[]) message.getPayload());
        log.info("Received order creation event: [{}]", payload);

        try {
            var orderDto = parsePayload(payload);

            var orderEntity = mapOrderDtoToEntity(orderDto);

            orderService.process(orderEntity);
        } catch (Exception ex) {
            log.error("Failed to precess message");
            throw new OrderCreationMessageProcessingFailed("Message failed", ex);
        }

        Optional.of(message.getHeaders())
                .map(headers -> headers.get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                        BasicAcknowledgeablePubsubMessage.class))
                .ifPresent(BasicAcknowledgeablePubsubMessage::ack);
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
