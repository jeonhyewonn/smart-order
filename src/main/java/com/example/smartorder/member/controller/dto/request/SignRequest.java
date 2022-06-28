package com.example.smartorder.member.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class SignRequest {
    @NotBlank
    @Size(max=20)
    private String accessId;

    @NotBlank
    @Size(min=8, max=20)
    private String password;
}
