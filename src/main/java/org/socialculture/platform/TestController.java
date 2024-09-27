package org.socialculture.platform;

import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.ErrorStatus;
import org.socialculture.platform.global.apiResponse.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test") // 예외 발생 상황 - INTERNAL_SERVER_ERROR
    public ApiResponse<Void> getSuccess(){
        int num = 2;
        if(num == 1) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR); // 500 에러 발생
        }
        return ApiResponse.onSuccess();
    }

    @GetMapping("/custom") // 커스텀 성공 응답
    public ApiResponse<String> getSuccessCustom(){
        String result = "custom test 성공!!";
        return ApiResponse.onSuccess(HttpStatus.BAD_GATEWAY, "COMMON400", "성공인줄 알았지", result); // 502 에러와 함께 커스텀 메시지
    }

    @GetMapping("/hi") // 고정된 성공 응답
    public ApiResponse<String> getTest(){
        String str = "고정된 성공응답";
        return ApiResponse.onSuccess(str); // 200 성공 응답
    }

    @GetMapping("/illegal-argument") // IllegalArgumentException 발생
    public ApiResponse<Void> triggerIllegalArgumentException() {
        String param = null;
        if(param == null) {
            throw new IllegalArgumentException("잘못된 인자입니다."); // IllegalArgumentException 던지기
        }
        return ApiResponse.onSuccess();
    }

    @GetMapping("/null-pointer") // NullPointerException 발생
    public ApiResponse<Void> triggerNullPointerException() {
        String str = null;
        if(str.equals("test")) { // str이 null이므로 NullPointerException 발생
            return ApiResponse.onSuccess();
        }
        return ApiResponse.onFailure(HttpStatus.BAD_REQUEST, "NULL_POINTER", "NullPointerException이 발생했습니다.", null);
    }

    @GetMapping("/validation-error") // 데이터 검증 실패
    public ApiResponse<Void> triggerValidationError() {
        int age = -1; // 나이가 음수인 경우 데이터 검증 실패로 간주
        if(age < 0) {
            throw new GeneralException(ErrorStatus.LOGIN_FAIL); // 400 에러 발생
        }
        return ApiResponse.onSuccess();
    }
}
