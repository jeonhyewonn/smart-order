package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.message.PublisherMessage;
import com.example.smartorder.order.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderItemMessage implements PublisherMessage {
    private Long id;
    private Long itemId;
    private Integer quantity;

    public CreateOrderItemMessage(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.itemId = orderItem.getItem().getId();
        this.quantity = orderItem.getQuantity();
    }
}
