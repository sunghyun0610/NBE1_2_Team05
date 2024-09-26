package org.socialculture.platform;

import lombok.Getter;
import org.socialculture.platform.global.apiResponse.ApiResponse;
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
        return ApiResponse.onSuccess(HttpStatus.CREATED,"성공",result);
    }

}
