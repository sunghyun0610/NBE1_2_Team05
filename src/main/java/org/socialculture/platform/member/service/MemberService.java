package org.socialculture.platform.member.service;

import jakarta.servlet.http.HttpSession;
import org.socialculture.platform.member.entity.MemberEntity;

public interface MemberService {

    // 일반사용자 회원가입
//    MemberEntity registerBasicUser(LocalRegisterRequest localRegisterRequest);

    // 소셜사용자 회원가입
//    MemberEntity registerSocialUser(SocialRegisterRequest socialRegisterRequest);

    // 소셜 사용자 가입 확인
//    boolean isSocialMemberRegistered(SocialMemberCheckDto socialMemberCheckDto, HttpSession session);

    // 소셜 사용자 닉네임 확인
    boolean isSocialNicknameAvailable(String name);

    // 이메일 중복 확인
    boolean isEmailInUse(String email);

    // 닉네임 중복 확인
    boolean isNameInUse(String name);



}
