package com.example.smartorder.order.controller;

import com.example.smartorder.order.controller.dto.request.OrderRequest;
import com.example.smartorder.order.controller.dto.response.OrderResponse;
import com.example.smartorder.order.controller.dto.response.OrderIdResponse;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.service.OrderService;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import com.example.smartorder.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public OrderIdResponse putOrder(Authentication authentication, @RequestBody @Valid OrderRequest request) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();
        OrderCommand newOrder = OrderCommand.builder()
                .orderItemCommands(
                        request.getItems().stream()
                                .map(orderItem ->
                                        OrderItemCommand.builder()
                                                .itemId(orderItem.getId())
                                                .quantity(orderItem.getQuantity())
                                                .build()
                                )
                                .collect(toList())
                )
                .totalAmount(request.getPayAmount())
                .build();

        Order order = this.orderService.putOrder(memberId, newOrder);

        return new OrderIdResponse(order.getId());
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(Authentication authentication, @PathVariable("id") String orderId) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();

        Order order = this.orderService.getOrder(memberId, orderId);

        return new OrderResponse(order);
    }

    @PutMapping("/{id}/cancel")
    public void cancelOrder(Authentication authentication, @PathVariable("id") String orderId) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();

        this.orderService.cancelOrder(memberId, orderId);
    }
}
