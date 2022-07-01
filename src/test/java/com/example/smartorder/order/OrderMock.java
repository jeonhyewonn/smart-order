package com.example.smartorder.order;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.InsufficientIngredientException;
import com.example.smartorder.member.MemberMock;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.RejectionOrderMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;

import java.util.List;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class OrderMock {
    private final Item item = new ItemMock().item;

    public final OrderCommand orderCmd = OrderCommand.builder()
            .orderItemCommands(List.of(
                    OrderItemCommand.builder()
                            .itemId(item.getId())
                            .quantity(2)
                            .build()
            ))
            .totalAmount((double) 10000)
            .build();

    public final Order order = Order.createBy(
            orderCmd,
            new MemberMock().member,
            List.of(item)
    );

    public final CreateOrderMessage createOrderMessage = new CreateOrderMessage(this.order);
    public final DeliverOrderMessage deliverOrderMessage = new DeliverOrderMessage(this.order);
    public final RejectionOrderMessage rejectionOrderMessage = new RejectionOrderMessage(this.order, InsufficientIngredientException.class.getSimpleName());

    public OrderMock() {
        setField(this.order, "id", 1L);
    }
}