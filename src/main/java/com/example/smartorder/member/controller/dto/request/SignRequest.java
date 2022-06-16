package com.example.smartorder.member.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class SignRequest {
    @NotNull
    @Size(max=20)
    private String accessId;

    @NotNull
    @Size(min=8, max=20)
    private String password;
}
