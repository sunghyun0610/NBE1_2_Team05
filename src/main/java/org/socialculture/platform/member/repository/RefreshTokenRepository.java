package org.socialculture.platform.member.repository;

import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByMember(MemberEntity member);

    @Transactional
    void deleteAllByMember(MemberEntity member);
}
