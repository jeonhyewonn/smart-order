package com.example.smartorder.member.repository;

import com.example.smartorder.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        this.em.persist(member);
    }

    public Member findByAccessId(String accessId) {
        return this.em.createQuery("SELECT u FROM Member u WHERE u.accessId = :accessId", Member.class)
                .setParameter("accessId", accessId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Member findById(String id) {
        return this.em.find(Member.class, id);
    }
}
