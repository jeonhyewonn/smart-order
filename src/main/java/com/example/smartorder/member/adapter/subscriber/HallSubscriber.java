package com.example.smartorder.member.adapter.subscriber;

import com.example.smartorder.member.adapter.publisher.HallPublisher;
import com.example.smartorder.member.service.MemberNotiService;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.RejectionOrderMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HallSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();
    private final MemberNotiService memberNotiService;
    private final HallPublisher hallPublisher;

    @KafkaListener(topics = "delivery")
    public void deliverOrder(String message) {
        try {
            DeliverOrderMessage deliverOrderMessage = this.mapper.readValue(message, DeliverOrderMessage.class);
            this.memberNotiService.notifyToPickup(deliverOrderMessage);
            this.hallPublisher.successToDelivery(deliverOrderMessage);
        } catch (Exception e) {
            this.hallPublisher.failToDelivery(message, e);
        }
    }

    @KafkaListener(topics = "rejection")
    public void notifyRejection(String message) {
        try {
            RejectionOrderMessage rejectionOrderMessage = this.mapper.readValue(message, RejectionOrderMessage.class);
            this.memberNotiService.notifyRejection(rejectionOrderMessage);
        } catch (Exception e) {
            log.error("Fail To Notify Rejection With Message: ${}", message);
        }
    }
}
