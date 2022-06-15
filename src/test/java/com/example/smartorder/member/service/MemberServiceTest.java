package com.example.smartorder.member.service;

import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private MemberMock memberMock;

    public MemberServiceTest() {
        this.memberRepository = Mockito.mock(MemberRepository.class);
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.memberService = new MemberService(this.memberRepository, this.passwordEncoder);
    }

    @BeforeEach
    public void beforeEach() {
        this.memberMock = new MemberMock();
    }

    @Test
    public void joinWithExistingMemberWillFail() {
        // Given
        JoinMemberCommand joinMember = JoinMemberCommand.builder()
                .accessId("existingId")
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.Thirty)
                .gender(Gender.Women)
                .tel("01012345678")
                .build();
        Member existingMember = Member.createBy(joinMember, this.passwordEncoder.encode(joinMember.getPassword()));
        when(this.memberRepository.findByAccessId(joinMember.getAccessId())).thenReturn(existingMember);

        // When
        try {
            this.memberService.join(joinMember);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void joinWillSucceed() {
        // Given
        String accessId = "newId";
        JoinMemberCommand joinMember = JoinMemberCommand.builder()
                .accessId(accessId)
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.Thirty)
                .gender(Gender.Women)
                .tel("01012345678")
                .build();
        when(this.memberRepository.findByAccessId(joinMember.getAccessId())).thenReturn(null);

        // When
        Member member = this.memberService.join(joinMember);

        // Then
        assertThat(joinMember.getAccessId()).isEqualTo(member.getAccessId());
    }

    @Test
    public void loginWithUnknownMemberWillFail() {
        // Given
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId("unknownAccessId")
                .password("unknownPassword!")
                .build();
        when(this.memberRepository.findByAccessId(loginMember.getAccessId())).thenReturn(null);

        // When
        try {
            this.memberService.login(loginMember);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void loginWithIncorrectPasswordWillFail() {
        // Given
        Member existingMember = this.memberMock.member;
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId(existingMember.getAccessId())
                .password("incorrectPassword!")
                .build();
        when(this.memberRepository.findByAccessId(loginMember.getAccessId())).thenReturn(existingMember);

        // When
        try {
            this.memberService.login(loginMember);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void loginWillSucceed() {
        // Given
        String existingRawPassword = this.memberMock.joinMemberCmd.getPassword();
        Member existingMember = this.memberMock.member;
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId(existingMember.getAccessId())
                .password(existingRawPassword)
                .build();
        when(this.memberRepository.findByAccessId(loginMember.getAccessId())).thenReturn(existingMember);

        // When
        Member member = this.memberService.login(loginMember);

        // Then
        assertThat(loginMember.getAccessId()).isEqualTo(member.getAccessId());
    }

    @Test
    public void getMemberWithUnknownMemberWillFail() {
        // Given
        String memberId = UUID.randomUUID().toString();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // When
        try {
            this.memberService.getMember(memberId);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void getMemberWillSucceed() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // When
        Member member = this.memberService.getMember(memberId);

        // Then
        assertThat(member).isEqualTo(existingMember);
    }

    @Test
    public void updateProfileWithUnknownMemberWillFail() {
        // Given
        String memberId = UUID.randomUUID().toString();
        UpdateProfileCommand profile = UpdateProfileCommand
                .builder()
                .name("newName")
                .ageGroup(AgeGroup.Forty)
                .gender(Gender.Women)
                .tel("010-4321-8765")
                .build();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // When
        try {
            this.memberService.updateProfile(memberId, profile);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void updateProfileWillSucceed() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        UpdateProfileCommand profile = UpdateProfileCommand
                .builder()
                .name("newName")
                .tel("010-4321-8765")
                .build();
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // When
        this.memberService.updateProfile(memberId, profile);

        // Then
        Member updatedMember = this.memberRepository.findById(memberId);
        assertThat(updatedMember.getName()).isEqualTo(profile.getName());
        assertThat(updatedMember.getTel()).isEqualTo(profile.getTel());
    }

    @Test
    public void changePasswordWithUnknownMemberWillFail() {
        // Given
        String memberId = UUID.randomUUID().toString();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // When
        try {
            this.memberService.changePassword(memberId, oriPassword, newPassword);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void changePasswordWithIncorrectOldPasswordWillFail() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // When
        try {
            this.memberService.changePassword(memberId, oriPassword, newPassword);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void changePasswordWillSucceed() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        String oriPassword = this.memberMock.joinMemberCmd.getPassword();
        String newPassword = "newPassword";
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // When
        this.memberService.changePassword(memberId, oriPassword, newPassword);

        // Then
        Member updatedMember = this.memberRepository.findById(memberId);
        assertThat(this.passwordEncoder.matches(newPassword, updatedMember.getPassword())).isEqualTo(true);
    }

    @Test
    public void deactivateAccountWithUnknownMemberWillFail() {
        // Given
        String memberId = UUID.randomUUID().toString();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // When
        try {
            this.memberService.deactivateAccount(memberId);
        } catch (IllegalStateException e) {
            return;
        }

        // Then
        fail();
    }

    @Test
    public void deactivateAccountWillSucceed() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // When
        this.memberService.deactivateAccount(memberId);

        // Then
        Member deactivatedMember = this.memberRepository.findById(memberId);
        assertThat(deactivatedMember.getIsDeleted()).isEqualTo(true);
    }
}