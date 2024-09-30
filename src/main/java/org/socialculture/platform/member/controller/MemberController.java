package org.socialculture.platform.member.controller;


import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.dto.request.LocalRegisterRequest;
import org.socialculture.platform.member.dto.response.RegisterResponse;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 이메일 중복 체크
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@PathVariable("email") String email) {
        boolean emailInUse = memberService.isEmailInUse(email);
        if (emailInUse) {
            throw new GeneralException(ErrorStatus.EMAIL_DUPLICATE);
        }
        return ApiResponse.onSuccess(true);
    }

    // 닉네임 중복 체크
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<ApiResponse<Boolean>> checkName(@PathVariable("name") String name) {
        boolean nameInUse = memberService.isNameInUse(name);
        if (nameInUse) {
            throw new GeneralException(ErrorStatus.NAME_DUPLICATE);
        }
        return ApiResponse.onSuccess(true);
    }


    // 일반 사용자 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestBody LocalRegisterRequest localRegisterRequest) {

            RegisterResponse registerResponse = memberService.registerBasicUser(localRegisterRequest);
            return ApiResponse.onSuccess(registerResponse);
    }


}
