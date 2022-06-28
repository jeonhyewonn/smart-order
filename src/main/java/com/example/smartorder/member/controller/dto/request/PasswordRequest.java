package com.example.smartorder.member.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordRequest {
    @NotBlank
    private String oriPassword;

    @NotBlank
    private String newPassword;
}
