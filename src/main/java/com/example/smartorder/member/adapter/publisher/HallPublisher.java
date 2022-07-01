package com.example.smartorder.member.adapter.publisher;

import com.example.smartorder.adapter.broker.MessageBroker;
import com.example.smartorder.adapter.broker.message.FailResponseMessage;
import com.example.smartorder.adapter.broker.message.SuccessResponseMessage;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HallPublisher {
    @Value("${message-broker.topic.delivery-success}")
    private String successDeliveryTopic;

    @Value("${message-broker.topic.delivery-fail}")
    private String failedDeliveryTopic;

    private final MessageBroker broker;

    public void successToDelivery(DeliverOrderMessage orderMessage) {
        SuccessResponseMessage message = new SuccessResponseMessage(orderMessage.getId());
        this.broker.publish(this.successDeliveryTopic, message);
    }

    public void failToDelivery(String receivedMessage, Exception e) {
        FailResponseMessage message = new FailResponseMessage(receivedMessage, e);
        this.broker.publish(this.failedDeliveryTopic, message);
    }
}
