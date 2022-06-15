package com.example.smartorder.member.service;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UUID join(JoinMemberCommand joinMember) {
        Member existingMember = this.memberRepository.findByAccessId(joinMember.getAccessId());
        if (existingMember != null) throw new IllegalStateException("ExistingMember");

        Member newMember = Member.createBy(joinMember, this.passwordEncoder.encode(joinMember.getPassword()));
        this.memberRepository.save(newMember);

        return newMember.getId();
    }

    public UUID login(LoginMemberCommand loginMember) {
        Member existingMember = this.memberRepository.findByAccessId(loginMember.getAccessId());
        if (existingMember == null) throw new IllegalStateException("UnknownMember");
        if (!this.passwordEncoder.matches(loginMember.getPassword(), existingMember.getPassword())) throw new IllegalStateException("IncorrectPassword");

        return existingMember.getId();
    }

    public Member getMember(UUID id) {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new IllegalStateException("UnknownMember");

        return member;
    }

    @Transactional
    public void updateProfile(UUID id, UpdateProfileCommand profile) {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new IllegalStateException("UnknownMember");

        member.updateProfile(profile);
    }

    @Transactional
    public void changePassword(UUID id, String oriPassword, String newPassword) {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new IllegalStateException("UnknownMember");
        if (!this.passwordEncoder.matches(oriPassword, member.getPassword())) throw new IllegalStateException("IncorrectPassword");

        member.changePassword(this.passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void deactivateAccount(UUID id) {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new IllegalStateException("UnknownMember");

        member.deactivate();
    }
}
