package com.example.smartorder.order.service;

import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.order.OrderMock;
import com.example.smartorder.order.adapter.publisher.CounterPublisher;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.domain.OrderState;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class OrderOrchestrationServiceTest {
    private final OrderRepository orderRepository;
    private final OrderOrchestrationService orderOrchestrationService;
    private final Member member = new MemberMock().member;
    private OrderMock orderMock;

    public OrderOrchestrationServiceTest() {
        this.orderRepository = Mockito.mock(OrderRepository.class);
        CounterPublisher counterPublisher = Mockito.mock(CounterPublisher.class);
        this.orderOrchestrationService = new OrderOrchestrationService(
                this.orderRepository,
                counterPublisher
        );
    }

    @BeforeEach
    public void beforeEach() {
        this.orderMock = new OrderMock();
    }

    @Test
    void deliverOrderThrowNotFoundOrderException() {
        // Given
        Long orderId = 0L;
        when(this.orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> {
            this.orderOrchestrationService.deliverOrder(orderId);
        }).isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    void deliverOrderWillSucceed() {
        // Given
        Long orderId = this.orderMock.order.getId();
        when(this.orderRepository.findById(orderId)).thenReturn(Optional.of(this.orderMock.order));

        // When
        this.orderOrchestrationService.deliverOrder(orderId);

        // Then
        Order releasedOrder = this.orderRepository.findById(orderId).get();
        assertThat(releasedOrder.getState()).isEqualTo(OrderState.RELEASED);
    }

    private String getRejectionCause() {
        return "InsufficientIngredients";
    }

    @Test
    void rejectOrderThrowNotFoundOrderException() {
        // Given
        CreateOrderMessage orderMessage = orderMock.createOrderMessage;
        Long orderId = 0L;
        setField(orderMessage, "id", orderId);
        when(this.orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> {
            this.orderOrchestrationService.rejectOrder(orderMessage, getRejectionCause());
        }).isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    void rejectOrderWillSucceed() {
        // Given
        CreateOrderMessage orderMessage = orderMock.createOrderMessage;
        Long orderId = orderMessage.getId();
        when(this.orderRepository.findById(orderId)).thenReturn(Optional.of(this.orderMock.order));

        // When
        this.orderOrchestrationService.rejectOrder(orderMessage, getRejectionCause());

        // Then
        Order rejectedOrder = this.orderRepository.findById(orderId).get();
        assertThat(rejectedOrder.getState()).isEqualTo(OrderState.REJECTED);
    }
}