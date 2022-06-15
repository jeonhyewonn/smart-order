package com.example.smartorder.member.controller.dto;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class SignUpRequest extends SignRequest {
    @NotNull
    @Size(max=20)
    private String name;

    // TODO: enum validation
    private String ageGroup;
    private String gender;
    private String tel;

    public AgeGroup getAgeGroup() {
        return this.ageGroup != null
                ? AgeGroup.valueOf(this.ageGroup)
                : null;
    }

    public Gender getGender() {
        return this.gender != null
                ? Gender.valueOf(this.gender)
                : null;
    }
}
