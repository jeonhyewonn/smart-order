package com.example.smartorder.order.domain;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;

    private Boolean isDeleted;

    public static OrderItem createBy(OrderItemCommand newOrderItem, Order order, Item item) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID().toString());
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(newOrderItem.getQuantity());
        orderItem.setIsDeleted(false);

        return orderItem;
    }
}
