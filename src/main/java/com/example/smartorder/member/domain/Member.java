package com.example.smartorder.member.domain;

import com.example.smartorder.order.domain.Order;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
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
@Table(name = "members")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public static Member createBy(JoinMemberCommand newMember, String encodedPassword) {
        Member member = new Member();
        member.setId(UUID.randomUUID());
        member.setAccessId(newMember.getAccessId());
        member.setPassword(encodedPassword);
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

    public void changePassword(String password) {
        this.setPassword(password);
    }

    public void deactivate() {
        this.setIsDeleted(true);
    }
}
