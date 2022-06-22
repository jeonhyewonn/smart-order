package com.example.smartorder.member.domain;

import com.example.smartorder.order.domain.Order;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "members")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    private String id;

    private String accessId;

    @Embedded
    private Password password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String tel;

    private Boolean isDeleted;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public static Member createBy(JoinMemberCommand newMember, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setId(UUID.randomUUID().toString());
        member.setAccessId(newMember.getAccessId());
        member.setPassword(Password.createBy(newMember.getPassword(), passwordEncoder));
        member.setRole(Role.CLIENT);
        member.setName(newMember.getName());
        member.setAgeGroup(newMember.getAgeGroup());
        member.setGender(newMember.getGender());
        member.setTel(newMember.getTel());
        member.setIsDeleted(false);
        member.setCreatedAt(LocalDateTime.now());

        return member;
    }

    public void updateProfile(UpdateProfileCommand profile) {
        if (profile.getName() != null) this.setName(profile.getName());
        if (profile.getAgeGroup() != null) this.setAgeGroup(profile.getAgeGroup());
        if (profile.getGender() != null) this.setGender(profile.getGender());
        if (profile.getTel() != null) this.setTel(profile.getTel());
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.setPassword(Password.createBy(password, passwordEncoder));
    }

    public void deactivate() {
        this.setIsDeleted(true);
    }
}
