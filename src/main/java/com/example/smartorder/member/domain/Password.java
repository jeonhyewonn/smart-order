package com.example.smartorder.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    private String encodedWord;

    public static Password createBy(String rawPassword, PasswordEncoder passwordEncoder) {
        Password password = new Password();
        password.setEncodedWord(passwordEncoder.encode(rawPassword));

        return password;
    }

    public Boolean isMatchedWith(String cmpPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(cmpPassword, this.getEncodedWord());
    }
}
