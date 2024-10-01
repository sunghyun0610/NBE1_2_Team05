package org.socialculture.platform.member.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.SocialRegisterRequest;
import org.socialculture.platform.member.dto.response.RegisterResponse;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.SocialProvider;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.member.validator.EmailValidator;
import org.socialculture.platform.member.validator.NameValidator;
import org.socialculture.platform.member.validator.PasswordValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final EmailValidator emailValidator;
    private final NameValidator nameValidator;
    private final PasswordValidator passwordValidator;


    public MemberServiceImpl(MemberRepository memberRepository, EmailValidator emailValidator,
                             NameValidator nameValidator, PasswordValidator passwordValidator) {
        this.memberRepository = memberRepository;
        this.emailValidator = emailValidator;
        this.nameValidator = nameValidator;
        this.passwordValidator = passwordValidator;
    }


    /**
     * 일반 사용자 회원가입
     * @param localRegisterRequest
     * @return JWT Token
     */
    @Override
    public RegisterResponse registerBasicUser(LocalRegisterRequest localRegisterRequest) {
        return registerUser(localRegisterRequest.toEntity());
    }


    /**
     * 소셜 사용자 회원가입
     * @param socialRegisterRequest
     * @return JWT Token
     */
    @Override
    public RegisterResponse registerSocialUser(SocialRegisterRequest socialRegisterRequest) {
        return registerUser(socialRegisterRequest.toEntity());
    }


    /**
     * 일반,소셜 사용자 모두 회원가입 진행
     * @param member
     * @return JwtToken
     */
    @Transactional
    protected RegisterResponse registerUser(MemberEntity member) {
        if (isEmailInUse(member.getEmail())) {
            throw new GeneralException(ErrorStatus.EMAIL_DUPLICATE);
        }
        if (isNameInUse(member.getName())) {
            throw new GeneralException(ErrorStatus.NAME_DUPLICATE);
        }
        if (member.getProvider() == SocialProvider.LOCAL &&  // 일반 사용자 회원가입에서만 검증
                !passwordValidator.isValidPassword(member.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_INVALID);
        }
        MemberEntity savedMember = memberRepository.save(member);
        // JWT 토큰 발행 이후 수정
        RegisterResponse registerResponse = new RegisterResponse("jwtToken");
        return registerResponse;
    }


    /**
     * 소셜 사용자가 가입되어있는지 확인 없으면 회원가입 진행에 필요한 정보들 session에 저장
     * @param socialMemberCheckDto
     * @param session
     * @return true이면 가입되어있는 사용자, false면 가입되어있지 않는 사용자
     */
    @Override
    public boolean isSocialMemberRegistered(SocialMemberCheckDto socialMemberCheckDto,
                                            HttpSession session) {
        Optional<MemberEntity> findMember = memberRepository.findByEmail(socialMemberCheckDto.email());

        return findMember.map(member -> {
            boolean matchesProviderId = member.getProviderId().equals(socialMemberCheckDto.providerId());
            if (!matchesProviderId) {
                throw new GeneralException(ErrorStatus.SOCIAL_EMAIL_DUPLICATE);
            }
            return true;
        }).orElseGet(() -> {
            session.setAttribute("providerId", socialMemberCheckDto.providerId());
            session.setAttribute("email", socialMemberCheckDto.email());
            session.setAttribute("provider", socialMemberCheckDto.provider());
            return false;
        });
    }


    /**
     * 이메일 중복 확인(이메일 형식 검증도 같이확인)
     * @param email
     * @return true면 이메일이 이미 사용중인것
     */
    @Override
    public boolean isEmailInUse(String email) {
        if (!emailValidator.isValidEmail(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_INVALID);
        }
        return memberRepository.existsByEmail(email);
    }


    /**
     * 닉네임 중복 확인 (닉네임 형식 검증도 같이확인)
     * @param name
     * @return true면 닉네임이 이미 사용중인것
     */
    @Override
    public boolean isNameInUse(String name) {
        if (!nameValidator.isValidName(name)) {
            throw new GeneralException(ErrorStatus.NAME_DUPLICATE);
        }
        return memberRepository.existsByName(name);
    }



}
