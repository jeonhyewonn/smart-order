package com.example.smartorder.user.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    private String accessId;
    private String password;

    public static Account createBy(String accessId, String plainPassword) {
        Account account = new Account();
        account.setAccessId(accessId);
        account.setPassword(plainPassword); // TODO: encryption

        return account;
    }
}
