package org.socialculture.platform.member.service;

import jakarta.servlet.http.HttpSession;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.MemberCategoryRequest;
import org.socialculture.platform.member.dto.response.CategoryResponse;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;

import java.util.List;

public interface MemberService {

    // 일반사용자 회원가입
    void registerBasicUser(LocalRegisterRequest localRegisterRequest);

    // 소셜 사용자 가입여부 확인
    boolean isSocialMemberRegistered(SocialMemberCheckDto socialMemberCheckDto,
                                     HttpSession session
    );

    // 소셜 사용자 닉네임 확인 후 회원가입
    void registerSocialUserFromSession(SocialMemberInfoDto memberInfoDto,
                                       HttpSession session);

    // 이메일 검증과 중복 체크
    void validateEmailAndCheckDuplicate(String email);

    // 닉네임 검증과 중복 체크
    void validateNameAndCheckDuplicate(String name);

    // 닉네임 변경
    void updateName(String email, String name);

    // 사용자 선호 카테고리 추가
    void memberAddCategory(MemberCategoryRequest memberCategoryRequest, String email);

    // 카테고리 전체 조회
    List<CategoryResponse> getAllCategories();

    // 사용자 선호 카테고리 목록 조회
    List<CategoryResponse> getFavoriteCategories(String email);

    // 선호 카테고리 수정
    void updateFavoriteCategories(MemberCategoryRequest memberCategoryRequest,
                                  String email);
}
