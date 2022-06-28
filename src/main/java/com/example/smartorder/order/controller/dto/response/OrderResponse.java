package com.example.smartorder.order.controller.dto.response;

import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderState;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class OrderResponse {
    private final List<OrderItemResponse> items;
    private final Double payAmount;
    private final Boolean isCanceled;
    private final OrderState state;
    private final String createdAt;

    public OrderResponse(Order order) {
        this.items = order.getOrderItems().stream().map(OrderItemResponse::new).collect(toList());
        this.payAmount = order.getTotalAmount();
        this.isCanceled = order.getIsCanceled();
        this.state = order.getState();
        this.createdAt = order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
