package com.example.smartorder.adapter.broker;

public interface MessageBroker {
    public void publish(String topic, PublisherMessage message);
}
