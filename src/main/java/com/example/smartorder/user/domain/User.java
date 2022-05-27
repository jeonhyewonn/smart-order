package com.example.smartorder.user.domain;

import com.example.smartorder.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    private String id;
    @Embedded
    private Account account;
    private String name;
    private AgeGroup ageGroup;
    private Gender gender;
    private String tel;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}
