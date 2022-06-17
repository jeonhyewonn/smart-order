package com.example.smartorder.order.service;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.UnknownItemException;
import com.example.smartorder.item.repository.ItemRepository;
import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.UnknownMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderItem;
import com.example.smartorder.order.exception.IncorrectTotalAmountException;
import com.example.smartorder.order.repository.OrderRepository;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    public OrderServiceTest() {
        this.orderRepository = Mockito.mock(OrderRepository.class);
        this.memberRepository = Mockito.mock(MemberRepository.class);
        this.itemRepository = Mockito.mock(ItemRepository.class);
        this.orderService = new OrderService(this.orderRepository, this.memberRepository, this.itemRepository);
    }

    @Test
    public void putOrderWithUnknownMemberThrowUnknownMemberException() {
        // Given
        String memberId = "unknownMemberId";
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId(ItemMock.item.getId())
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 10000)
                .build();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // When
        try {
            this.orderService.putOrder(memberId, order);
        } catch (UnknownMemberException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void putOrderWithWrongItemThrowUnknownItemException() {
        // Given
        Member member = new MemberMock().member;
        String memberId = member.getId();
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId("wrongItemId")
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 10000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(member);

        String[] itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).toArray(String[]::new);
        when(this.itemRepository.findByIds(itemIds)).thenReturn(List.of());

        // When
        try {
            this.orderService.putOrder(memberId, order);
        } catch (UnknownItemException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void putOrderWithWrongTotalAmountThrowIncorrectTotalAmountException() {
        // Given
        Member member = new MemberMock().member;
        String memberId = member.getId();
        Item item = ItemMock.item;
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId(item.getId())
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 20000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(member);

        String[] itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).toArray(String[]::new);
        when(this.itemRepository.findByIds(itemIds)).thenReturn(List.of(item));

        // When
        try {
            this.orderService.putOrder(memberId, order);
        } catch (IncorrectTotalAmountException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void putOrderWillSucceed() {
        // Given
        Member member = new MemberMock().member;
        String memberId = member.getId();
        Item item = ItemMock.item;
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId(item.getId())
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 10000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(member);

        String[] itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).toArray(String[]::new);
        when(this.itemRepository.findByIds(itemIds)).thenReturn(List.of(item));

        // When
        Order putOrder = this.orderService.putOrder(memberId, order);

        // Then
        assertThat(putOrder.getTotalAmount()).isEqualTo(order.getTotalAmount());
        for (OrderItem putOrderItem : putOrder.getOrderItems()) {
            boolean hasOrderItem = order.getOrderItemCommands().stream().anyMatch(cmd -> {
                boolean isItemIdEqual = putOrderItem.getItem().getId().equals(cmd.getItemId());
                boolean isQuantityEqual = putOrderItem.getQuantity().equals(cmd.getQuantity());

                return isItemIdEqual && isQuantityEqual;
            });
            assertThat(hasOrderItem).isEqualTo(true);
        }
    }
}