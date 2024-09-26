package org.socialculture.platform;

import lombok.Getter;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.SuccessStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResponse<Void> getSuccess(){
        return ApiResponse.onSuccess();
    }
    @GetMapping("/custom")
    public ApiResponse<String> getSuccessCustom(){
        String result="custom test 성공!!";
        return ApiResponse.onSuccess(HttpStatus.BAD_GATEWAY,"성공인줄 알았지",result);
    }
    @GetMapping("/hi")
    public ApiResponse<String> getTest(){
        String result ="다양한 응답 값";
        return ApiResponse.onSuccess(HttpStatus.CREATED,"201생성",result);
    }

}
