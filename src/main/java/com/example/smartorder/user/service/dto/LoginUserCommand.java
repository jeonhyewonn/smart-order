package com.example.smartorder.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserCommand {
    private String accessId;
    private String password;
}
