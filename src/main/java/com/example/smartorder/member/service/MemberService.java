package com.example.smartorder.member.service;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.AlreadyExistingMemberException;
import com.example.smartorder.member.exception.IncorrectPasswordException;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member join(JoinMemberCommand joinMember) {
        Member existingMember = this.memberRepository.findByAccessId(joinMember.getAccessId());
        if (existingMember != null) throw new AlreadyExistingMemberException();

        Member newMember = Member.createBy(joinMember, this.passwordEncoder);
        this.memberRepository.save(newMember);

        return newMember;
    }

    public Member login(LoginMemberCommand loginMember) {
        Member existingMember = this.memberRepository.findByAccessId(loginMember.getAccessId());
        if (existingMember == null) throw new NotFoundMemberException();
        if (!existingMember.getPassword().isMatchedWith(loginMember.getPassword(), this.passwordEncoder)) throw new IncorrectPasswordException();

        return existingMember;
    }

    public Member getMember(String id) {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new NotFoundMemberException();

        return member;
    }

    @Transactional
    public void updateProfile(String id, UpdateProfileCommand profile) {
        Member member = this.getMember(id);

        member.updateProfile(profile);
    }

    @Transactional
    public void changePassword(String id, String oriPassword, String newPassword) {
        Member member = this.getMember(id);
        if (!member.getPassword().isMatchedWith(oriPassword, this.passwordEncoder)) throw new IncorrectPasswordException();

        member.changePassword(newPassword, this.passwordEncoder);
    }

    @Transactional
    public void deactivateAccount(String id) {
        Member member = this.getMember(id);

        member.deactivate();
    }
}
