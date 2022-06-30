package com.example.smartorder.order.adapter.publisher.dto;


import com.example.smartorder.adapter.broker.PublisherMessage;
import com.example.smartorder.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
public class CreateOrderMessage implements PublisherMessage {
    private Long id;
    private List<CreateOrderItemMessage> orderItems;

    public CreateOrderMessage(Order order) {
        this.id = order.getId();
        this.orderItems = order.getOrderItems().stream()
                .map(CreateOrderItemMessage::new)
                .collect(toList());
    }
}
