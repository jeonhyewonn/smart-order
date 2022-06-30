package com.example.smartorder.item.adapter.publisher;

import com.example.smartorder.adapter.broker.MessageBroker;
import com.example.smartorder.adapter.broker.message.FailResponseMessage;
import com.example.smartorder.adapter.broker.message.SuccessResponseMessage;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KitchenPublisher {
    @Value("${message-broker.topic.cooking-success}")
    private String successCookingTopic;

    @Value("${message-broker.topic.cooking-fail}")
    private String failedCookingTopic;

    private final MessageBroker broker;

    public void successToCook(CreateOrderMessage orderMessage) {
        SuccessResponseMessage message = new SuccessResponseMessage(orderMessage.getId());
        this.broker.publish(this.successCookingTopic, message);
    }

    public void failToCook(String receivedMessage, Exception e) {
        FailResponseMessage message = new FailResponseMessage(receivedMessage, e);
        this.broker.publish(this.failedCookingTopic, message);
    }
}
