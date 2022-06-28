package com.example.smartorder.member.service;

import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.AgeGroup;
import com.example.smartorder.member.domain.Gender;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.AlreadyExistingMemberException;
import com.example.smartorder.member.exception.IncorrectPasswordException;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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
    public void joinWithExistingMemberThrowAlreadyExistingMemberException() {
        // Given
        JoinMemberCommand joinMember = JoinMemberCommand.builder()
                .accessId("existingId")
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.THIRTY)
                .gender(Gender.WOMEN)
                .tel("01012345678")
                .build();
        Member existingMember = Member.createBy(joinMember, this.passwordEncoder);
        when(this.memberRepository.findByAccessId(joinMember.getAccessId())).thenReturn(existingMember);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.join(joinMember);
        }).isInstanceOf(AlreadyExistingMemberException.class);
    }

    @Test
    public void joinWillSucceed() {
        // Given
        String accessId = "newId";
        JoinMemberCommand joinMember = JoinMemberCommand.builder()
                .accessId(accessId)
                .password("testPassword!")
                .name("테스트")
                .ageGroup(AgeGroup.THIRTY)
                .gender(Gender.WOMEN)
                .tel("01012345678")
                .build();
        when(this.memberRepository.findByAccessId(joinMember.getAccessId())).thenReturn(null);

        // When
        Member member = this.memberService.join(joinMember);

        // Then
        assertThat(joinMember.getAccessId()).isEqualTo(member.getAccessId());
    }

    @Test
    public void loginWithUnknownMemberThrowNotFoundMemberException() {
        // Given
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId("unknownAccessId")
                .password("unknownPassword!")
                .build();
        when(this.memberRepository.findByAccessId(loginMember.getAccessId())).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.login(loginMember);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void loginWithIncorrectPasswordThrowIncorrectPasswordException() {
        // Given
        Member existingMember = this.memberMock.member;
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId(existingMember.getAccessId())
                .password("incorrectPassword!")
                .build();
        when(this.memberRepository.findByAccessId(loginMember.getAccessId())).thenReturn(existingMember);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.login(loginMember);
        }).isInstanceOf(IncorrectPasswordException.class);
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
    public void getMemberWithUnknownMemberThrowNotFoundMemberException() {
        // Given
        String memberId = UUID.randomUUID().toString();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.getMember(memberId);
        }).isInstanceOf(NotFoundMemberException.class);
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
    public void updateProfileWithUnknownMemberThrowNotFoundMemberException() {
        // Given
        String memberId = UUID.randomUUID().toString();
        UpdateProfileCommand profile = UpdateProfileCommand
                .builder()
                .name("newName")
                .ageGroup(AgeGroup.FORTY)
                .gender(Gender.WOMEN)
                .tel("010-4321-8765")
                .build();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.updateProfile(memberId, profile);
        }).isInstanceOf(NotFoundMemberException.class);
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
    public void changePasswordWithUnknownMemberThrowNotFoundMemberException() {
        // Given
        String memberId = UUID.randomUUID().toString();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.changePassword(memberId, oriPassword, newPassword);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void changePasswordWithIncorrectOldPasswordThrowIncorrectPasswordException() {
        // Given
        Member existingMember = this.memberMock.member;
        String memberId = existingMember.getId();
        String oriPassword = "oriPassword";
        String newPassword = "newPassword";
        when(this.memberRepository.findById(memberId)).thenReturn(existingMember);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.changePassword(memberId, oriPassword, newPassword);
        }).isInstanceOf(IncorrectPasswordException.class);
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
        assertThat(updatedMember.getPassword().isMatchedWith(newPassword, this.passwordEncoder)).isEqualTo(true);
    }

    @Test
    public void deactivateAccountWithUnknownMemberThrowNotFoundMemberException() {
        // Given
        String memberId = UUID.randomUUID().toString();
        when(this.memberRepository.findById(memberId)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> {
            this.memberService.deactivateAccount(memberId);
        }).isInstanceOf(NotFoundMemberException.class);
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