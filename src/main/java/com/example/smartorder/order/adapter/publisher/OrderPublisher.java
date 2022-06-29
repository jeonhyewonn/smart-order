package com.example.smartorder.order.adapter.publisher;

import com.example.smartorder.adapter.broker.MessageBroker;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderPublisher {
    @Value("${message-broker.topic.order}")
    private String topic;

    private final MessageBroker broker;

    public void createOrder(Order order) {
        CreateOrderMessage message = new CreateOrderMessage(order);
        this.broker.publish(this.topic, message);
    }
}
