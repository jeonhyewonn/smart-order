package com.example.smartorder.member.repository;

import com.example.smartorder.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        this.em.persist(member);
    }

    public Member findByAccessId(String accessId) {
        List<Member> results = this.em.createQuery("SELECT u FROM Member u WHERE u.account.accessId = :accessId", Member.class)
                .setParameter("accessId", accessId)
                .getResultList();
        if (results.isEmpty()) return null;

        return results.get(0);
    }

    public Member findById(UUID id) {
        return this.em.find(Member.class, id);
    }
}
