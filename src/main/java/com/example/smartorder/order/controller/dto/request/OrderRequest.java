package com.example.smartorder.order.controller.dto.request;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class OrderRequest {
    @Valid
    private List<OrderItemRequest> items;

    @NotNull
    private Double payAmount;
}
