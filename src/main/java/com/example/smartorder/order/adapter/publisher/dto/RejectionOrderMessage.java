package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.message.PublisherMessage;
import com.example.smartorder.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RejectionOrderMessage implements PublisherMessage {
    private Long id;
    private Long memberId;
    private String cause;

    public RejectionOrderMessage(Order order, String cause) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
        this.cause = cause;
    }
}
