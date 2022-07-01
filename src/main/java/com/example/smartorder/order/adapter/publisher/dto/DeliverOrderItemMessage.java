package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.message.PublisherMessage;
import com.example.smartorder.order.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliverOrderItemMessage implements PublisherMessage {
    private String name;
    private Integer quantity;

    public DeliverOrderItemMessage(OrderItem orderItem) {
        this.name = orderItem.getItem().getName();
        this.quantity = orderItem.getQuantity();
    }
}
