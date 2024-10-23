package org.socialculture.platform.member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.MemberCategoryRequest;
import org.socialculture.platform.member.dto.response.CategoryResponse;
import org.socialculture.platform.member.dto.response.MemberInfoResponse;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /**
     * 이메일 중복 체크
     *
     * @return true면 중복x
     */
    @GetMapping("/validation/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@PathVariable("email") String email) {
        memberService.validateEmailAndCheckDuplicate(email);
        return ApiResponse.onSuccess(true);
    }

    /**
     * 닉네임 중복 체크
     *
     * @param name
     * @return true면 중복x
     */
    @GetMapping("/validation/name/{name}")
    public ResponseEntity<ApiResponse<Boolean>> checkName(@PathVariable("name") String name) {
        memberService.validateNameAndCheckDuplicate(name);
        return ApiResponse.onSuccess(true);
    }


    /**
     * 일반사용자 회원가입
     *
     * @param localRegisterRequest
     * @return 성공 200ok
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody LocalRegisterRequest localRegisterRequest) {

        memberService.registerBasicUser(localRegisterRequest);
        return ApiResponse.onSuccess();
    }

    /**
     * 닉네임 변경
     *
     * @param name
     * @param token
     * @return 성공 200, 닉네임중복409 , 닉네임 형식안맞음400
     */
    @PatchMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Void>> updateNickName(
            @PathVariable String name, @AuthenticationPrincipal UserDetails userDetails) {

        memberService.updateName(userDetails.getUsername(), name);
        return ApiResponse.onSuccess();
    }

    /**
     * 사용자 선호 카테고리 등록
     *
     * @param memberCategoryRequest
     * @return 성공 200, 카테고리 및 회원 못찾음 404
     */
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Void>> addFavoriteCategories(
            @RequestBody MemberCategoryRequest memberCategoryRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (memberCategoryRequest.categories().size() > 3) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        memberService.memberAddCategory(memberCategoryRequest, userDetails.getUsername());
        return ApiResponse.onSuccess();
    }


    /**
     * 카테고리 전체 목록 조회
     *
     * @return categoryId, nameKr, nameEn
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> allCategories = memberService.getAllCategories();
        return ApiResponse.onSuccess(allCategories);
    }


    /**
     * 사용자 선호 카테고리 조회
     *
     * @param token
     * @return
     */
    @GetMapping("/categories/favorites")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getFavoriteCategories(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<CategoryResponse> favoriteCategories = memberService.getFavoriteCategories(email);
        return ApiResponse.onSuccess(favoriteCategories);
    }


    /**
     * 사용자 선호 카테고리 수정
     *
     * @param memberCategoryRequest
     * @return
     */
    @PutMapping("/categories/favorites")
    public ResponseEntity<ApiResponse<Void>> updateFavoriteCategories(
            @RequestBody MemberCategoryRequest memberCategoryRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        if (memberCategoryRequest.categories().size() > 3) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        memberService.updateFavoriteCategories(memberCategoryRequest, userDetails.getUsername());

        return ApiResponse.onSuccess();
    }


    /**
     * 사용자 정보 조회
     *
     * @param userDetails
     * @return email, name, role
     */
    @GetMapping
    public ResponseEntity<ApiResponse<MemberInfoResponse>> getMemberInfoByEmail(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ApiResponse.onSuccess(memberService.getMemberInfoByEmail(email));
    }


    /**
     * 첫 로그인 시 첫 로그인 여부 변경
     *
     * @param userDetails
     * @return
     */
    @PatchMapping("/first-login")
    public ResponseEntity<ApiResponse<Void>> changeFirstLogin(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        memberService.changeFirstLogin(email);
        return ApiResponse.onSuccess();
    }
}
