package com.example.smartorder.security;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessorService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = this.memberRepository.findById(id);
        if (member == null) throw new UsernameNotFoundException("UnknownAccessor");

        return new Accessor(member);
    }
}
