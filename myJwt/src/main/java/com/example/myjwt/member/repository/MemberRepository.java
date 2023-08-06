package com.example.myjwt.member.repository;

import com.example.myjwt.member.entity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<TestMember, Long> {

    Boolean existsByMemberEmail(String memberEmail);

    Optional<TestMember> findByMemberEmail(String memberEmail);
}
