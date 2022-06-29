package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.PublisherMessage;
import com.example.smartorder.order.domain.OrderItem;
import lombok.Getter;

@Getter
public class CreateOrderItemMessage implements PublisherMessage {
    private final Long id;
    private final Long itemId;
    private final Integer quantity;

    public CreateOrderItemMessage(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.itemId = orderItem.getItem().getId();
        this.quantity = orderItem.getQuantity();
    }
}
