package com.example.smartorder.member.controller.dto.request;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ProfileRequest {
    @NotBlank
    @Size(max=20)
    private String name;

    private AgeGroup ageGroup;
    private Gender gender;

    private String tel;
}
