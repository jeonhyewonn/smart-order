package com.example.smartorder.order.adapter.publisher;

import com.example.smartorder.adapter.broker.MessageBroker;
import com.example.smartorder.order.adapter.publisher.dto.CookedOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.FailedOrderMessage;
import com.example.smartorder.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderPublisher {
    @Value("${message-broker.topic.order}")
    private String orderTopic;

    @Value("${message-broker.topic.failed-order}")
    private String failedOrderTopic;

    @Value("${message-broker.topic.dish}")
    private String dishTopic;

    private final MessageBroker broker;

    public void createOrder(Order order) {
        CreateOrderMessage message = new CreateOrderMessage(order);
        this.broker.publish(this.orderTopic, message);
    }

    public void completeDish(Order order) {
        CookedOrderMessage message = new CookedOrderMessage(order);
        this.broker.publish(this.dishTopic, message);
    }

    public void failToCook(String orderMessage, Exception e) {
        FailedOrderMessage message = new FailedOrderMessage(orderMessage, e);
        this.broker.publish(this.failedOrderTopic, message);
    }
}
