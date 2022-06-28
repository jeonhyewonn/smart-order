package com.example.smartorder.order.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class OrderItemRequest {
    @NotNull
    private Long id;

    @NotNull
    private Integer quantity;
}
