package com.example.smartorder.member.repository;

import com.example.smartorder.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT u FROM Member u WHERE u.accessId = :accessId")
    public Optional<Member> findByAccessId(@Param("accessId") String accessId);

    public Optional<Member> findById(Long id);
}
