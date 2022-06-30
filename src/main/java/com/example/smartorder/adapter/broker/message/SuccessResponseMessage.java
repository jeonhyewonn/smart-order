package com.example.smartorder.adapter.broker.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SuccessResponseMessage implements PublisherMessage {
    private final Long id;
}
