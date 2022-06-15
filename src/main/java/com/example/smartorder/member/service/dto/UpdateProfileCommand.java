package com.example.smartorder.member.service.dto;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileCommand {
    private String name;
    private AgeGroup ageGroup;
    private Gender gender;
    private String tel;
}
