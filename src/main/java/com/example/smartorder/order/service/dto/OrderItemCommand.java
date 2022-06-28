package com.example.smartorder.order.service.dto;

import com.example.smartorder.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemCommand {
    private Long itemId;
    private Integer quantity;

    public Double getPayAmount(Item item) {
        return item.getPrice() * this.getQuantity();
    }
}
