package org.socialculture.platform.member.auth.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.auth.dto.CodeRequestDto;
import org.socialculture.platform.member.auth.dto.EmailRequestDto;
import org.socialculture.platform.member.auth.service.EmailVerificationService;
import org.socialculture.platform.member.validator.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/emails")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final EmailValidator emailValidator;


    /**
     * 입력한 이메일로 인증코드를 요청
     * @param emailRequestDto
     * @param session
     * @return
     * @throws MessagingException
     */
    @PostMapping("/code")
    public ResponseEntity<ApiResponse<Void>> getVerifyCode(
            @RequestBody EmailRequestDto emailRequestDto, HttpSession session) throws MessagingException {

        String email = emailRequestDto.email();

        // 이메일 형식 검증
        if (!emailValidator.isValidEmail(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_INVALID);
        }

        // 이미 인증에 사용된 이메일인지 확인
        if (emailVerificationService.verifyEmailDuplicate(email)) {
            throw new GeneralException(ErrorStatus.VERIFICATION_EMAIL_DUPLICATE);
        }

        session.setAttribute("verifyEmail", email);
        session.setMaxInactiveInterval(600); // 세션 10분간 유지

        emailVerificationService.sendEmail(email);
        return ApiResponse.onSuccess();
    }



    /**
     * 이메일 인증을 위한 코드 인증
     * @param codeRequestDto
     * @param userDetails
     * @param session
     * @return
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestBody CodeRequestDto codeRequestDto,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {

        String code = codeRequestDto.code();

        if (code == null || session.getAttribute("verifyEmail") == null || userDetails.getUsername() == null) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        if(emailVerificationService.verifyCode(
                userDetails.getUsername(), (String) session.getAttribute("verifyEmail"), code)){
            session.invalidate();
            return ApiResponse.onSuccess();
        };

        session.invalidate();
        throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
    }



}