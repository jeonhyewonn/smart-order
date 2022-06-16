package com.example.smartorder.member.controller;

import com.example.smartorder.member.controller.dto.request.PasswordRequest;
import com.example.smartorder.member.controller.dto.request.ProfileRequest;
import com.example.smartorder.member.controller.dto.request.SignRequest;
import com.example.smartorder.member.controller.dto.request.SignUpRequest;
import com.example.smartorder.member.controller.dto.response.ProfileResponse;
import com.example.smartorder.member.controller.dto.response.SignResponse;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.service.MemberService;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import com.example.smartorder.security.Accessor;
import com.example.smartorder.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public SignResponse signUp(@RequestBody @Valid SignUpRequest request) {
        JoinMemberCommand joinMember = JoinMemberCommand.builder()
                .accessId(request.getAccessId())
                .password(request.getPassword())
                .name(request.getName())
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .tel(request.getTel())
                .build();

        Member member = this.memberService.join(joinMember);
        String token = this.jwtTokenProvider.generateToken(member);

        return new SignResponse(token);
    }

    @PostMapping("/sign-in")
    public SignResponse signIn(@RequestBody @Valid SignRequest request) {
        LoginMemberCommand loginMember = LoginMemberCommand.builder()
                .accessId(request.getAccessId())
                .password(request.getPassword())
                .build();

        Member member = this.memberService.login(loginMember);
        String token = this.jwtTokenProvider.generateToken(member);

        return new SignResponse(token);
    }

    @GetMapping("/members/me/profile")
    public ProfileResponse getProfile(Authentication authentication) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();

        Member member = this.memberService.getMember(memberId);

        return new ProfileResponse(member);
    }

    @PatchMapping("/members/me/profile")
    public void updateProfile(Authentication authentication, @RequestBody @Valid ProfileRequest request) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();
        UpdateProfileCommand profile = UpdateProfileCommand.builder()
                .name(request.getName())
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .tel(request.getTel())
                .build();

        this.memberService.updateProfile(memberId, profile);
    }

    @PutMapping("/members/me/password")
    public void changePassword(Authentication authentication, @RequestBody @Valid PasswordRequest request) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();

        this.memberService.changePassword(memberId, request.getOriPassword(), request.getNewPassword());
    }

    @DeleteMapping("/members/me")
    public void deleteAccount(Authentication authentication) {
        String memberId = ((Accessor) authentication.getPrincipal()).getUsername();

        this.memberService.deactivateAccount(memberId);
    }
}
