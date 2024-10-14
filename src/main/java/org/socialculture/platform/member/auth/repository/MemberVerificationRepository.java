package org.socialculture.platform.member.auth.repository;

import org.socialculture.platform.member.auth.entity.MemberVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberVerificationRepository extends JpaRepository<MemberVerificationEntity, Long> {

    boolean existsByVerificationEmail(String verificationEmail);
}
