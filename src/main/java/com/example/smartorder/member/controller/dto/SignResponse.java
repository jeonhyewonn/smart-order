package com.example.smartorder.member.controller.dto;

import lombok.Getter;

@Getter
public class SignResponse {
    private final String accessToken;

    public SignResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
