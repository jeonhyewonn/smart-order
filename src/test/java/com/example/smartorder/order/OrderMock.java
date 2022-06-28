package com.example.smartorder.order;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.member.MemberMock;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;

import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

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
            orderCmd,
            new MemberMock().member,
            List.of(ItemMock.item)
    );

    public OrderMock() {
        setField(this.order, "id", 1L);
    }
}