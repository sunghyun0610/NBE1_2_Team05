package org.socialculture.platform.member.auth.repository;

import org.socialculture.platform.member.auth.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity,Long> {

    Optional<EmailVerificationEntity> findByEmail(String email);
    void deleteByEmail(String email);
}
