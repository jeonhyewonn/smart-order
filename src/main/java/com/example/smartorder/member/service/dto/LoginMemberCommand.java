package com.example.smartorder.member.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginMemberCommand {
    private String accessId;
    private String password;
}
