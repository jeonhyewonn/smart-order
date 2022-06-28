package com.example.smartorder.order.controller.dto.response;

import com.example.smartorder.order.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private final Long id;
    private final Integer quantity;

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getItem().getId();
        this.quantity = orderItem.getQuantity();
    }
}
