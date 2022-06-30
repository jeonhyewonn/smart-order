package com.example.smartorder.order.adapter.publisher;

import com.example.smartorder.adapter.broker.MessageBroker;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.RejectionOrderMessage;
import com.example.smartorder.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CounterPublisher {
    @Value("${message-broker.topic.cooking}")
    private String cookingTopic;

    @Value("${message-broker.topic.delivery}")
    private String deliveryTopic;

    @Value("${message-broker.topic.rejection}")
    private String rejectionTopic;

    private final MessageBroker broker;

    public void createOrder(Order order) {
        CreateOrderMessage message = new CreateOrderMessage(order);
        this.broker.publish(this.cookingTopic, message);
    }

    public void deliverOrder(Order order) {
        DeliverOrderMessage message = new DeliverOrderMessage(order);
        this.broker.publish(this.deliveryTopic, message);
    }

    public void notifyRejection(Order order, String cause) {
        RejectionOrderMessage message = new RejectionOrderMessage(order, cause);
        this.broker.publish(this.rejectionTopic, message);
    }
}
