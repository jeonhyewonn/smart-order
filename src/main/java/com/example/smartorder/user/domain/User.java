package com.example.smartorder.user.domain;

import com.example.smartorder.order.domain.Order;
import com.example.smartorder.user.service.dto.JoinUserCommand;
import com.example.smartorder.user.service.dto.UpdateProfileCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private UUID id;
    private String accessId;
    private String password;
    private String name;
    private AgeGroup ageGroup;
    private Gender gender;
    private String tel;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public static User createBy(JoinUserCommand newUser, String encodedPassword) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setAccessId(newUser.getAccessId());
        user.setPassword(encodedPassword);
        user.setName(newUser.getName());
        user.setAgeGroup(newUser.getAgeGroup());
        user.setGender(newUser.getGender());
        user.setTel(newUser.getTel());
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public void updateProfile(UpdateProfileCommand profile) {
        if (profile.getName() != null) this.setName(profile.getName());
        if (profile.getAgeGroup() != null) this.setAgeGroup(profile.getAgeGroup());
        if (profile.getGender() != null) this.setGender(profile.getGender());
        if (profile.getTel() != null) this.setTel(profile.getTel());
    }

    public void changePassword(String password) {
        this.setPassword(password);
    }

    public void deactivate() {
        this.setIsDeleted(true);
    }
}
