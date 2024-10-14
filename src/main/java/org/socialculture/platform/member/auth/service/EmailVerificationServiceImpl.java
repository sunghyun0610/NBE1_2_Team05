package org.socialculture.platform.member.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.member.auth.entity.EmailVerificationEntity;
import org.socialculture.platform.member.auth.entity.MemberVerificationEntity;
import org.socialculture.platform.member.auth.repository.EmailVerificationRepository;
import org.socialculture.platform.member.auth.repository.MemberVerificationRepository;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

/**
 * @author kim yechan
 * 공연관리자 권한 획득을 위한 이메일 인증
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberVerificationRepository memberVerificationRepository;
    private final MemberService memberService;
    private final TemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String senderEmail;


    // 보낼 이메일 폼 생성, 이미 DB에 있다면 삭제
    @Transactional
    protected MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("인증번호를 somun페이지에 입력해주세요.");
        message.setFrom(senderEmail);
        message.setText(setContext(authCode), "utf-8", "html");


        // DB에 인증코드 저장 (기존 인증 정보 삭제 후 새로 생성)
        emailVerificationRepository.findByEmail(email).ifPresent(existingVerification -> {
            emailVerificationRepository.deleteByEmail(email);
            log.info("이전에 신청한 이메일 인증 정보를 삭제했습니다. {}", email);
        });

        EmailVerificationEntity emailVerification = new EmailVerificationEntity(email, authCode, 5L);
        emailVerificationRepository.save(emailVerification);

        return message;
    }

    // 랜덤으로 6자리 인증 코드 생성
    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 보낼 이메일 내용 초기화
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);
    }



    /**
     * 이메일로 인증코드 발송
     * @param toEmail
     * @throws MessagingException
     */
    @Transactional
    @Override
    public void sendEmail(String toEmail) throws MessagingException {
        MimeMessage emailForm = createEmailForm(toEmail);
        javaMailSender.send(emailForm);
    }




    /**
     * 이메일 인증을 하는 사용자가 보내온 code를 인증
     * 만약 맞다면 해당 회원의 권한을 공연관리자로 수정
     * @param registeredEmail
     * @param verificationEmail
     * @param code
     * @return
     */
    @Transactional
    @Override
    public boolean verifyCode(String registeredEmail, String verificationEmail, String code) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findByEmail(verificationEmail)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 인증시도 데이터가 존재하지않습니다."));

        // 인증 성공(코드의 유효시간과 인증코드를 확인)
        if (emailVerificationEntity.isExpired() && emailVerificationEntity.getVerificationCode().equals(code)) {
            // MemberVerification 테이블에 추가
            log.info("인증코드 성공");

            MemberVerificationEntity memberVerificationEntity = MemberVerificationEntity.builder()
                    .verificationEmail(verificationEmail)
                    .memberEmail(registeredEmail)
                    .build();

            memberVerificationRepository.save(memberVerificationEntity);

            // 유저의 권한을 ROLE_PADMIN으로 변경
            memberService.changeRoleToPadmin(registeredEmail);
            log.info("유저 권한 변경");

            // 기존 EmailVerification 테이블에서 삭제
            emailVerificationRepository.deleteByEmail(verificationEmail);
            return true;
        }
        return false;
    }




    /**
     * 이메일 인증에 사용된 이메일인지 확인
     * @param email
     * @return
     */
    @Override
    public boolean verifyEmailDuplicate(String email) {
        return memberVerificationRepository.existsByVerificationEmail(email);
    }




}
