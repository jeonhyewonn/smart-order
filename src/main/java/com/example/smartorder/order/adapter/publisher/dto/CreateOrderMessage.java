package com.example.smartorder.order.adapter.publisher.dto;


import com.example.smartorder.adapter.broker.PublisherMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderState;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class CreateOrderMessage implements PublisherMessage {
    private final Long id;
    private final Long memberId;
    private final List<CreateOrderItemMessage> orderItems;
    private final OrderState state;

    public CreateOrderMessage(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
        this.orderItems = order.getOrderItems().stream()
                .map(CreateOrderItemMessage::new)
                .collect(toList());
        this.state = order.getState();
    }
}
