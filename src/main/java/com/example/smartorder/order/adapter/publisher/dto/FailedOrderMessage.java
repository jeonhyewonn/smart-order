package com.example.smartorder.order.adapter.publisher.dto;

import com.example.smartorder.adapter.broker.PublisherMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FailedOrderMessage implements PublisherMessage {
    public String receivedMessage;
    public String cause;

    public FailedOrderMessage(String receivedMessage, Exception e) {
        this.receivedMessage = receivedMessage;
        this.cause = e.getMessage();
    }
}
