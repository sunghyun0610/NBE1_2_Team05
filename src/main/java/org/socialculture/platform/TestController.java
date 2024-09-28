package org.socialculture.platform;

import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class TestController {

    @GetMapping("/test") // 예외 발생 상황 - INTERNAL_SERVER_ERROR
    public ResponseEntity<ApiResponse<Void>> getSuccess(){
        int num = 2;
        if(num == 1) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR); // 500 에러 발생
        }
        return ApiResponse.onSuccess();
    }

    @GetMapping("/custom") // 커스텀 성공 응답
    public ResponseEntity<ApiResponse<String>> getSuccessCustom(){
        String result = "custom test 성공!!";
        return ApiResponse.onSuccess(HttpStatus.CREATED, "COMMON400", "생성일 경우", result); // 502 에러와 함께 커스텀 메시지
    }

    @GetMapping("/hi") // 고정된 성공 응답
    public ResponseEntity<ApiResponse<String>> getTest(){
        String str = "고정된 성공응답";
        return ApiResponse.onSuccess(str); // 200 성공 응답
    }

    @GetMapping("/illegal-argument") // IllegalArgumentException 발생
    public ResponseEntity<ApiResponse<Void>> triggerIllegalArgumentException() {
        String param = null;
        if(param == null) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);// IllegalArgumentException 던지기
        }
        return ApiResponse.onSuccess();
    }

    @GetMapping("/null-pointer") // NullPointerException 발생
    public ResponseEntity<ApiResponse<Void>> triggerNullPointerException() {
        String str = null;
        if(str.equals("test")) { // str이 null이므로 NullPointerException 발생
            return ApiResponse.onSuccess();
        }
        throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/validation-error") // 데이터 검증 실패
    public ResponseEntity<ApiResponse<Void>> triggerValidationError() {
        int age = -1; // 나이가 음수인 경우 데이터 검증 실패로 간주
        if(age < 0) {
            throw new GeneralException(ErrorStatus.LOGIN_FAIL); // 400 에러 발생
        }
        return ApiResponse.onSuccess();
    }


}
