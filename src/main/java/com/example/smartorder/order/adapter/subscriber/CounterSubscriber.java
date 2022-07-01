package com.example.smartorder.order.adapter.subscriber;

import com.example.smartorder.adapter.broker.message.FailResponseMessage;
import com.example.smartorder.adapter.broker.message.SuccessResponseMessage;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import com.example.smartorder.order.service.OrderOrchestrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CounterSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderOrchestrationService orderOrchestrationService;

    @KafkaListener(topics = "cooking-success")
    public void handleSuccessCooking(String message) {
        try {
            SuccessResponseMessage successResponse = this.mapper.readValue(message, SuccessResponseMessage.class);
            this.orderOrchestrationService.deliverOrder(successResponse.getId());
        } catch (Exception e) {
            // TODO: notify to developer (messenger)
            log.error("fail to handle success cooking: ${}", message);
        }
    }

    @KafkaListener(topics = "cooking-fail")
    public void handleFailedCooking(String message) {
        try {
            FailResponseMessage failResponse = this.mapper.readValue(message, FailResponseMessage.class);
            CreateOrderMessage createOrderMessage = this.mapper.readValue(failResponse.getReceivedMessage(), CreateOrderMessage.class);
            this.orderOrchestrationService.rejectOrder(createOrderMessage.getId(), failResponse.getCause());
        } catch (Exception e) {
            // TODO: notify to developer (messenger)
            log.error("fail to handle failed cooking: ${}", message);
        }
    }

    @KafkaListener(topics = "delivery-success")
    public void handleSuccessDelivery(String message) {
        try {
            SuccessResponseMessage successResponse = this.mapper.readValue(message, SuccessResponseMessage.class);
            this.orderOrchestrationService.completeOrder(successResponse.getId());
        } catch (Exception e) {
            // TODO: notify to developer (messenger)
            log.error("fail to handle success delivery: ${}", message);
        }
    }

    @KafkaListener(topics = "delivery-fail")
    public void handleFailedDelivery(String message) {
        try {
            FailResponseMessage failResponse = this.mapper.readValue(message, FailResponseMessage.class);
            DeliverOrderMessage deliverOrderMessage = this.mapper.readValue(failResponse.getReceivedMessage(), DeliverOrderMessage.class);
            this.orderOrchestrationService.rejectOrder(deliverOrderMessage.getId(), failResponse.getCause());
        } catch (Exception e) {
            // TODO: notify to developer (messenger)
            log.error("fail to handle failed delivery: ${}", message);
        }
    }
}
