package com.example.smartorder.member.controller.dto.response;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import com.example.smartorder.member.domain.Member;
import lombok.Getter;

@Getter
public class ProfileResponse {
    private final String name;
    private final AgeGroup ageGroup;
    private final Gender gender;
    private final String tel;

    public ProfileResponse(Member member) {
        this.name = member.getName();
        this.ageGroup = member.getAgeGroup();
        this.gender = member.getGender();
        this.tel = member.getTel();
    }
}
