package com.example.smartorder.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Gender {
    MEN,
    WOMEN;

    @JsonCreator
    public static Gender toEnum(String value) {
        if (value == null) return null;

        return Arrays.stream(Gender.values())
                .filter(gender -> gender.name().equals(value.toUpperCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
