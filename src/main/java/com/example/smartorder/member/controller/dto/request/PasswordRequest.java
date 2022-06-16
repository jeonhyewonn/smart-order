package com.example.smartorder.member.controller.dto.request;

import lombok.Getter;

@Getter
public class PasswordRequest {
    private String oriPassword;
    private String newPassword;
}
