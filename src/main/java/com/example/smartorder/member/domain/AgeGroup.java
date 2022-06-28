package com.example.smartorder.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum AgeGroup {
    TWENTY,
    THIRTY,
    FORTY,
    FIFTY,
    SIXTY,
    OVER_SEVENTY;

    @JsonCreator
    public static AgeGroup toEnum(String value) {
        if (value == null) return null;

        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> ageGroup.name().equals(value.toUpperCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
