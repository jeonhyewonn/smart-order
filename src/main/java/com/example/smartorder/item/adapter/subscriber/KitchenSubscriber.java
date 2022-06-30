package com.example.smartorder.item.adapter.subscriber;

import com.example.smartorder.item.adapter.publisher.KitchenPublisher;
import com.example.smartorder.item.service.ItemCookService;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KitchenSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ItemCookService itemCookService;
    private final KitchenPublisher kitchenPublisher;

    @KafkaListener(topics = "cooking") // TODO: 환경변수화
    public void cookItem(String message) {
        try {
            CreateOrderMessage createOrderMessage = this.mapper.readValue(message, CreateOrderMessage.class);
            this.itemCookService.cook(createOrderMessage.getOrderItems());
            this.kitchenPublisher.successToCook(createOrderMessage);
        } catch (Exception e) {
            this.kitchenPublisher.failToCook(message, e);
        }
    }
}
