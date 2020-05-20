package ua.stepess.dnipro.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrderCreationMessageHandler implements MessageHandler {

    @Override
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void handleMessage(Message<?> message) throws MessagingException {
        var payload = new String((byte[]) message.getPayload());
        log.info("Received order creation event: [{}]", payload);

        Optional.of(message.getHeaders())
                .map(headers -> headers.get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                        BasicAcknowledgeablePubsubMessage.class))
                .ifPresent(BasicAcknowledgeablePubsubMessage::ack);
    }

}
