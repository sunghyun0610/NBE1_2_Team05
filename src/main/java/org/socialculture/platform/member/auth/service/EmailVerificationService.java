package org.socialculture.platform.member.auth.service;

import jakarta.mail.MessagingException;
import org.springframework.transaction.annotation.Transactional;

public interface EmailVerificationService {
    // 인증코드 이메일 발송
    @Transactional
    void sendEmail(String toEmail) throws MessagingException;

    // 코드 검증
    @Transactional
    boolean verifyCode(String registeredEmail, String verificationEmail, String code);

    // 이미 인증에 사용된 이메일인지 확인
    boolean verifyEmailDuplicate(String email);
}
