package org.socialculture.platform.member.service;

import jakarta.servlet.http.HttpSession;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.SocialRegisterRequest;
import org.socialculture.platform.member.dto.response.RegisterResponse;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;

public interface MemberService {

    // 일반사용자 회원가입
    RegisterResponse registerBasicUser(LocalRegisterRequest localRegisterRequest);

    // 소셜사용자 회원가입
    RegisterResponse registerSocialUser(SocialRegisterRequest socialRegisterRequest);

    // 소셜 사용자 가입 확인
    boolean isSocialMemberRegistered(SocialMemberCheckDto socialMemberCheckDto, HttpSession session);

    // 이메일 중복 확인
    boolean isEmailInUse(String email);

    // 닉네임 중복 확인
    boolean isNameInUse(String name);



}
