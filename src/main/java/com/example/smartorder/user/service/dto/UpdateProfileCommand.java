package com.example.smartorder.user.service.dto;

import com.example.smartorder.user.domain.AgeGroup;
import com.example.smartorder.user.domain.Gender;
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
