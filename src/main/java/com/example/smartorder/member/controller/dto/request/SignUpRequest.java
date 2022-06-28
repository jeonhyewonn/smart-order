package com.example.smartorder.member.controller.dto.request;

import lombok.Getter;

import javax.validation.Valid;

@Getter
public class SignUpRequest extends SignRequest {
    @Valid
    private ProfileRequest profile;
}
