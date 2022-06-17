package com.example.smartorder.order.domain;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Double totalAmount;
    private OrderState state;
    private Boolean isCanceled;
    private LocalDateTime createdAt;

    public static Order createBy(OrderCommand newOrder, Member member, List<Item> items) {
        HashMap<String, Item> itemMap = new HashMap<>();
        for(Item item : items) {
            itemMap.put(item.getId(), item);
        }
        List<OrderItem> orderItems = newOrder.getOrderItemCommands().stream()
                .map(cmd -> OrderItem.createBy(cmd, itemMap.get(cmd.getItemId())))
                .collect(Collectors.toList());

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setMember(member);
        order.setOrderItems(orderItems);
        order.setTotalAmount(newOrder.getTotalAmount());
        order.setState(OrderState.ACCEPTED);
        order.setIsCanceled(false);
        order.setCreatedAt(LocalDateTime.now());

        return order;
    }
}
