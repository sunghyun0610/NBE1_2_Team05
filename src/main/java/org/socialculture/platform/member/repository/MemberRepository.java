package org.socialculture.platform.member.repository;

import org.socialculture.platform.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 닉네임 중복 확인
    boolean existsByName(String name);

    // 이메일로 사용자 조회
    Optional<MemberEntity> findByEmail(String email);


}
