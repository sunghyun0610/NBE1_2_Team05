package org.socialculture.platform.member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.request.MemberCategoryRequest;
import org.socialculture.platform.member.dto.response.CategoryResponse;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
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
     * @return true면 중복x
     */
    @GetMapping("/validation/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@PathVariable("email") String email) {
        memberService.validateEmailAndCheckDuplicate(email);
        return ApiResponse.onSuccess(true);
    }

    /**
     * 닉네임 중복 체크
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
     * @param name
     * @param token
     * @return 성공 200, 닉네임중복409 , 닉네임 형식안맞음400
     */
    @PatchMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Void>> updateNickName(@PathVariable String name){
//        String jwt = token.substring(7); // Bearer 제거
//        String email = jwtService.getUserIdFromToken(jwt);
        String email = "test@gmail.com";

        memberService.updateName(email, name);
        return ApiResponse.onSuccess();
    }

    /**
     * 사용자 선호 카테고리 등록
     * @param memberCategoryRequest
     * @return 성공 200, 카테고리 및 회원 못찾음 404
     */
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Void>> addFavoriteCategories(
            @RequestBody MemberCategoryRequest memberCategoryRequest) {

//        String jwt = token.substring(7); // Bearer 제거
//        String email = jwtService.getUserIdFromToken(jwt);

        String email = "test@gmail.com";
        memberService.memberAddCategory(memberCategoryRequest, email);
        return ApiResponse.onSuccess();
    }


    /**
     * 카테고리 전체 목록 조회
     * @return categoryId, nameKr, nameEn
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> allCategories = memberService.getAllCategories();
        return ApiResponse.onSuccess(allCategories);
    }


    /**
     * 사용자 선호 카테고리 조회
     * @param token
     * @return
     */
    @GetMapping("/categories/favorites")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getFavoriteCategories() {

        // 임시로 토큰 처리
        String email = "test@gmail.com";
        List<CategoryResponse> favoriteCategories = memberService.getFavoriteCategories(email);
        return ApiResponse.onSuccess(favoriteCategories);
    }


    /**
     * 사용자 선호 카테고리 수정
     * @param memberCategoryRequest
     * @return
     */
    @PutMapping("/categories/favorites")
    public ResponseEntity<ApiResponse<Void>> updateFavoriteCategories(
            @RequestBody MemberCategoryRequest memberCategoryRequest){

        String email = "test@gmail.com";
        memberService.updateFavoriteCategories(memberCategoryRequest, email);

        return ApiResponse.onSuccess();
    }



    // 회원권한 수정


}
