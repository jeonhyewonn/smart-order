package com.example.smartorder.order.service;

import com.example.smartorder.item.domain.Item;
import com.example.smartorder.item.exception.InsufficientIngredientException;
import com.example.smartorder.item.exception.NotFoundItemException;
import com.example.smartorder.order.OrderMock;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderItem;
import com.example.smartorder.order.domain.OrderState;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class OrderCookingServiceTest {
    private final OrderRepository orderRepository;
    private final OrderCookingService orderCookingService;
    private OrderMock orderMock;

    public OrderCookingServiceTest() {
        this.orderRepository = Mockito.mock(OrderRepository.class);
        this.orderCookingService = new OrderCookingService(
                this.orderRepository
        );
    }

    @BeforeEach
    public void beforeEach() {
        this.orderMock = new OrderMock();
    }

    @Test
    public void cookWithNotFoundOrderThrowNotFoundOrderException() {
        // Given
        CreateOrderMessage orderMessage = this.orderMock.orderMessage;
        when(this.orderRepository.findById(orderMessage.getId()))
                .thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> {
            this.orderCookingService.cook(orderMessage);
        }).isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    public void cookWithNotFoundItemThrowNotFoundItemException() {
        // Given
        CreateOrderMessage orderMessage = this.orderMock.orderMessage;
        Order order = orderMock.order;
        setField(order, "orderItems", List.of());

        when(this.orderRepository.findById(orderMessage.getId()))
                .thenReturn(Optional.of(order));

        // Then
        assertThatThrownBy(() -> {
            this.orderCookingService.cook(orderMessage);
        }).isInstanceOf(NotFoundItemException.class);
    }

    @Test
    public void cookWithInsufficientIngredientsThrowInsufficientIngredientException() {
        // Given
        CreateOrderMessage orderMessage = this.orderMock.orderMessage;
        Order order = orderMock.order;

        Map<Long, Item> itemMap = new HashMap<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            itemMap.put(item.getId(), item);
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = itemMap.get(orderItem.getItem().getId());
            int maxIngredientStock = item.getItemIngredients().stream()
                    .map((itemIngredient) -> itemIngredient.getIngredient().getStock())
                    .max(Integer::compare)
                    .orElse(0);

            setField(orderItem, "quantity", maxIngredientStock + 1);
        }

        when(this.orderRepository.findById(orderMessage.getId()))
                .thenReturn(Optional.of(order));

        // Then
        assertThatThrownBy(() -> {
            this.orderCookingService.cook(orderMessage);
        }).isInstanceOf(InsufficientIngredientException.class);
    }

    @Test
    public void cookWillSucceed() {
        // Given
        CreateOrderMessage orderMessage = this.orderMock.orderMessage;
        Order order = orderMock.order;

        Map<Long, Item> itemMap = new HashMap<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem();
            itemMap.put(item.getId(), item);
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = itemMap.get(orderItem.getItem().getId());
            int minIngredientStock = item.getItemIngredients().stream()
                    .map((itemIngredient) -> itemIngredient.getIngredient().getStock())
                    .min(Integer::compare)
                    .orElse(0);

            setField(orderItem, "quantity", minIngredientStock);
        }

        when(this.orderRepository.findById(orderMessage.getId()))
                .thenReturn(Optional.of(order));

        // When
        this.orderCookingService.cook(orderMessage);

        // Then
        Order cookedOrder = this.orderRepository.findById(orderMessage.getId()).get();
        assertThat(cookedOrder.getState()).isEqualTo(OrderState.COOKED);
    }
}