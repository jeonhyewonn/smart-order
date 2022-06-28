package com.example.smartorder.item.controller.response;

import com.example.smartorder.item.domain.Item;
import lombok.Getter;

@Getter
public class ItemResponse {
    private final Long id;
    private final String name;
    private final Double price;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
    }
}
