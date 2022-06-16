package com.example.smartorder.member.controller.dto.request;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import lombok.Getter;

@Getter
public class ProfileRequest {
    private String name;
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
