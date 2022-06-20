package com.example.smartorder.order.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private List<OrderItemRequest> items;
    private Double payAmount;
}
