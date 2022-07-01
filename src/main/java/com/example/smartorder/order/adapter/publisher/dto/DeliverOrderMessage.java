package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.message.PublisherMessage;
import com.example.smartorder.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
public class DeliverOrderMessage implements PublisherMessage {
    private Long id;
    private Long memberId;
    private List<DeliverOrderItemMessage> orderItems;

    public DeliverOrderMessage(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getId();
        this.orderItems = order.getOrderItems().stream()
                .map(DeliverOrderItemMessage::new)
                .collect(toList());
    }
}
