package com.example.smartorder.member.controller;

import com.example.smartorder.member.controller.dto.SignRequest;
import com.example.smartorder.member.controller.dto.SignResponse;
import com.example.smartorder.member.controller.dto.SignUpRequest;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.service.MemberService;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public SignResponse signUp(@RequestBody @Valid SignUpRequest request) {
        JoinMemberCommand joinMember = JoinMemberCommand
                .builder()
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
        LoginMemberCommand loginMember = LoginMemberCommand
                .builder()
                .accessId(request.getAccessId())
                .password(request.getPassword())
                .build();

        Member member = this.memberService.login(loginMember);
        String token = this.jwtTokenProvider.generateToken(member);

        return new SignResponse(token);
    }
}
