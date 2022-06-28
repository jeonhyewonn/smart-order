package com.example.smartorder.order.service;

import com.example.smartorder.item.ItemMock;
import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.item.repository.ItemRepository;
import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.order.OrderMock;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderItem;
import com.example.smartorder.order.exception.IncorrectTotalAmountException;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import com.example.smartorder.order.service.dto.OrderCommand;
import com.example.smartorder.order.service.dto.OrderItemCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;
    private final Member member = new MemberMock().member;

    public OrderServiceTest() {
        this.orderRepository = Mockito.mock(OrderRepository.class);
        this.memberRepository = Mockito.mock(MemberRepository.class);
        this.itemRepository = Mockito.mock(ItemRepository.class);
        this.orderService = new OrderService(this.orderRepository, this.memberRepository, this.itemRepository);
    }

    @Test
    public void putOrderWithNotFoundMemberThrowNotFoundMemberException() {
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

        // Then
        assertThatThrownBy(() -> {
            this.orderService.putOrder(memberId, order);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void putOrderWithWrongItemThrowNotFoundItemException() {
        // Given
        String memberId = this.member.getId();
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId("wrongItemId")
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 10000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(this.member);

        List<String> itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).collect(toList());
        when(this.itemRepository.findByIds(itemIds)).thenReturn(List.of());

        // Then
        assertThatThrownBy(() -> {
            this.orderService.putOrder(memberId, order);
        }).isInstanceOf(NotFoundItemException.class);
    }

    @Test
    public void putOrderWithWrongTotalAmountThrowIncorrectTotalAmountException() {
        // Given
        String memberId = this.member.getId();
        Item item = ItemMock.item;
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId(item.getId())
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 20000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(this.member);

        List<String> itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).collect(toList());
        when(this.itemRepository.findByIds(itemIds)).thenReturn(List.of(item));

        // Then
        assertThatThrownBy(() -> {
            this.orderService.putOrder(memberId, order);
        }).isInstanceOf(IncorrectTotalAmountException.class);
    }

    @Test
    public void putOrderWillSucceed() {
        // Given
        String memberId = this.member.getId();
        Item item = ItemMock.item;
        OrderItemCommand orderItem = OrderItemCommand.builder()
                .itemId(item.getId())
                .quantity(2)
                .build();
        OrderCommand order = OrderCommand.builder()
                .orderItemCommands(Arrays.asList(orderItem))
                .totalAmount((double) 10000)
                .build();

        when(this.memberRepository.findById(memberId)).thenReturn(this.member);

        List<String> itemIds = order.getOrderItemCommands().stream().map(OrderItemCommand::getItemId).collect(toList());
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

    @Test
    public void getOrderWithWrongOrderIdThrowNotFoundOrderException() {
        // Given
        String orderId = "wrongOrderId";
        when(this.orderRepository.findById(orderId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.orderService.getOrder(this.member.getId(), orderId);
        }).isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    public void getOrderWithWrongMemberIdThrowNotFoundMemberException() {
        // Given
        String memberId = "wrongMemberId";
        Order order = new OrderMock().order;
        when(this.orderRepository.findById(order.getId())).thenReturn(order);

        // Then
        assertThatThrownBy(() -> {
            this.orderService.getOrder(memberId, order.getId());
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void getOrderWillSucceed() {
        // Given
        Order order = new OrderMock().order;
        when(this.orderRepository.findById(order.getId())).thenReturn(order);

        // When
        Order foundOrder = this.orderService.getOrder(order.getMember().getId(), order.getId());

        // Then
        assertThat(foundOrder).isEqualTo(order);
    }

    @Test
    public void cancelOrderWithWrongOrderIdThrowNotFoundOrderException() {
        // Given
        String orderId = "wrongOrderId";
        when(this.orderRepository.findById(orderId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.orderService.cancelOrder(this.member.getId(), orderId);
        }).isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    public void cancelOrderWithWrongMemberIdThrowNotFoundMemberException() {
        // Given
        String memberId = "wrongMemberId";
        Order order = new OrderMock().order;
        when(this.orderRepository.findById(order.getId())).thenReturn(order);

        // Then
        assertThatThrownBy(() -> {
            this.orderService.cancelOrder(memberId, order.getId());
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void cancelOrderWillSucceed() {
        // Given
        Order order = new OrderMock().order;
        when(this.orderRepository.findById(order.getId())).thenReturn(order);

        // When
        this.orderService.cancelOrder(order.getMember().getId(), order.getId());

        // Then
        Order canceledOrder = this.orderRepository.findById(order.getId());
        assertThat(canceledOrder.getIsCanceled()).isEqualTo(true);
    }
}