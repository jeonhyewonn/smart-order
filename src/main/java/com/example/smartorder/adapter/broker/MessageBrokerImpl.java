package com.example.smartorder.adapter.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageBrokerImpl implements MessageBroker {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void publish(String topic, PublisherMessage message) {
        try {
            this.kafkaTemplate.send(topic, this.mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(e.getMessage());
        }
    }
}
