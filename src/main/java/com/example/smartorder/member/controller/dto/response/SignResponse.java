package com.example.smartorder.member.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignResponse {
    private final String accessToken;
}
