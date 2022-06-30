package com.example.smartorder.order.adapter.subscriber;

import com.example.smartorder.order.adapter.publisher.OrderPublisher;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.domain.Order;
import com.example.smartorder.order.service.OrderCookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KitchenSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderCookingService orderCookingService;
    private final OrderPublisher orderPublisher;

    @KafkaListener(topics = "order") // TODO: 환경변수화
    public void cookOrder(String message) {
        try {
            CreateOrderMessage createOrderMessage = this.mapper.readValue(message, CreateOrderMessage.class);
            Order cookedOrder = this.orderCookingService.cook(createOrderMessage);
            this.orderPublisher.completeDish(cookedOrder);
        } catch (Exception e) {
            this.orderPublisher.failToCook(message, e);
        }
    }
}
