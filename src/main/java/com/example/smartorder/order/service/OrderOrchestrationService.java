package com.example.smartorder.order.service;

import com.example.smartorder.order.adapter.publisher.CounterPublisher;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.exception.NotFoundOrderException;
import com.example.smartorder.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderOrchestrationService {
    private final OrderRepository orderRepository;
    private final CounterPublisher counterPublisher;

    @Transactional
    public void deliverOrder(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(NotFoundOrderException::new);

        order.deliver();

        this.counterPublisher.deliverOrder(order);
    }

    @Transactional
    public void completeOrder(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(NotFoundOrderException::new);

        order.complete();
    }

    @Transactional
    public void rejectOrder(Long id, String cause) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(NotFoundOrderException::new);

        order.reject();

        this.counterPublisher.notifyRejection(order, cause);
    }
}
