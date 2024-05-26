package com.example.dlauth.domain.repository;

import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentCode(String studentCode);

    Optional<Member> findByPlatformId(String platformId);

    Optional<Member> findByPlatformIdAndPlatformType(String platformId, PlatformType platformType);

}
