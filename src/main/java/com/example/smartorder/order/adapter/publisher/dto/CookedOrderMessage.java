package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.PublisherMessage;
import com.example.smartorder.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CookedOrderMessage implements PublisherMessage {
    private Long id;
    private Long memberId;

    public CookedOrderMessage(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
    }
}
