package com.example.smartorder.order;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.member.MemberMock;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class OrderMock {
    public final OrderCommand orderCmd = OrderCommand.builder()
            .orderItemCommands(List.of(
                    OrderItemCommand.builder()
                            .itemId(ItemMock.item.getId())
                            .quantity(2)
                            .build()
            ))
            .totalAmount((double) 10000)
            .build();
    public final Order order = Order.createBy(
            this.orderCmd,
            new MemberMock().member,
            List.of(ItemMock.item)
    );
}