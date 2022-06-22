package com.example.smartorder.member;

import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@NoArgsConstructor
public class MemberMock {
    public final JoinMemberCommand joinMemberCmd = JoinMemberCommand.builder()
            .accessId("mockAccessId")
            .password("testPassword!")
            .name("테스트")
            .ageGroup(AgeGroup.THIRTY)
            .gender(Gender.WOMEN)
            .tel("01012345678")
            .build();
    public final Member member = Member.createBy(
            this.joinMemberCmd,
            PasswordEncoderFactories.createDelegatingPasswordEncoder()
    );
}
