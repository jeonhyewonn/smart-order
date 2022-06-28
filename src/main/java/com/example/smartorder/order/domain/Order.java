package com.example.smartorder.order.domain;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.order.service.dto.OrderCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Double totalAmount;
    private OrderState state;
    private Boolean isCanceled;
    private LocalDateTime createdAt;

    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public static Order createBy(OrderCommand newOrder, Member member, List<Item> items) {
        Order order = new Order();
        order.setMember(member);
        order.setTotalAmount(newOrder.getTotalAmount());
        order.setState(OrderState.ACCEPTED);
        order.setIsCanceled(false);
        order.setCreatedAt(LocalDateTime.now());

        HashMap<Long, Item> itemMap = new HashMap<>();
        for(Item item : items) {
            itemMap.put(item.getId(), item);
        }

        List<OrderItem> orderItems = newOrder.getOrderItemCommands().stream()
                .map(cmd -> OrderItem.createBy(cmd, order, itemMap.get(cmd.getItemId())))
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        return order;
    }

    public void cancel() {
        this.setIsCanceled(true);
    }
}
