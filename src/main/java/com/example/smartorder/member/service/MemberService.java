package com.example.smartorder.member.service;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.AlreadyExistingMemberException;
import com.example.smartorder.member.exception.IncorrectPasswordException;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.dto.JoinMemberCommand;
import com.example.smartorder.member.service.dto.LoginMemberCommand;
import com.example.smartorder.member.service.dto.UpdateProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(JoinMemberCommand joinMember) {
        Optional<Member> existingMember = this.memberRepository.findByAccessId(joinMember.getAccessId());
        if (existingMember.isPresent()) throw new AlreadyExistingMemberException();

        Member newMember = Member.createBy(joinMember, this.passwordEncoder);
        this.memberRepository.save(newMember);

        return newMember;
    }

    public Member login(LoginMemberCommand loginMember) {
        Member existingMember = this.memberRepository.findByAccessId(loginMember.getAccessId())
                .orElseThrow(NotFoundMemberException::new);;
        if (!existingMember.getPassword().isMatchedWith(loginMember.getPassword(), this.passwordEncoder)) throw new IncorrectPasswordException();

        return existingMember;
    }

    public Member getMember(Long id) {
        return this.memberRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);
    }

    @Transactional
    public void updateProfile(Long id, UpdateProfileCommand profile) {
        Member member = this.getMember(id);

        member.updateProfile(profile);
    }

    @Transactional
    public void changePassword(Long id, String oriPassword, String newPassword) {
        Member member = this.getMember(id);
        if (!member.getPassword().isMatchedWith(oriPassword, this.passwordEncoder)) throw new IncorrectPasswordException();

        member.changePassword(newPassword, this.passwordEncoder);
    }

    @Transactional
    public void deactivateAccount(Long id) {
        Member member = this.getMember(id);

        member.deactivate();
    }
}
