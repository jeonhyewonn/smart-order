package com.example.smartorder.adapter.broker;

import com.example.smartorder.adapter.broker.message.PublisherMessage;

public interface MessageBroker {
    public void publish(String topic, PublisherMessage message);
}
