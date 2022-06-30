package com.example.smartorder.adapter.broker.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FailResponseMessage implements PublisherMessage {
    public String receivedMessage;
    public String cause;

    public FailResponseMessage(String receivedMessage, Exception e) {
        this.receivedMessage = receivedMessage;
        this.cause = e.getMessage();
    }
}
