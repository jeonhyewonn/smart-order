package com.example.smartorder.order.adapter.subscriber;

import com.example.smartorder.adapter.broker.message.FailResponseMessage;
import com.example.smartorder.adapter.broker.message.SuccessResponseMessage;
import com.example.smartorder.order.adapter.publisher.dto.CreateOrderMessage;
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
            log.error("fail to handle failed order: ${}", message);
        }
    }

    @KafkaListener(topics = "cooking-fail")
    public void handleFailedCooking(String message) {
        try {
            FailResponseMessage failResponse = this.mapper.readValue(message, FailResponseMessage.class);
            CreateOrderMessage createOrderMessage = this.mapper.readValue(failResponse.getReceivedMessage(), CreateOrderMessage.class);
            this.orderOrchestrationService.rejectOrder(createOrderMessage, failResponse.getCause());
        } catch (Exception e) {
            // TODO: notify to developer (messenger)
            log.error("fail to handle failed order: ${}", message);
        }
    }
}
