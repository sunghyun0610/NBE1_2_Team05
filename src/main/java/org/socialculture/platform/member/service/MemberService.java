package org.socialculture.platform.member.service;

import jakarta.servlet.http.HttpSession;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.MemberCategoryRequest;
import org.socialculture.platform.member.dto.request.SocialRegisterRequest;
import org.socialculture.platform.member.dto.response.CategoryResponse;
import org.socialculture.platform.member.dto.response.MemberInfoResponse;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;

import java.util.List;

public interface MemberService {

    // 일반사용자 회원가입
    void registerBasicUser(LocalRegisterRequest localRegisterRequest);

    // 소셜 사용자 가입여부 확인
    boolean isSocialMemberRegistered(SocialMemberCheckDto socialMemberCheckDto);

    // 소셜 사용자 닉네임 확인 후 회원가입
    void registerSocialMember(SocialRegisterRequest request, HttpSession session);

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

    // 사용자 정보 조회 - 마이페이지
    MemberInfoResponse getMemberInfoByEmail(String email);

    // 공연관리자로 권한 변경
    void changeRoleToPadmin(String email);

    // 첫로그인 여부 변경
    void changeFirstLogin(String email);
}
