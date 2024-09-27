package org.socialculture.platform.global.apiResponse;

import org.springframework.http.HttpStatus;

public class ErrorResponseDto {
    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;

    // 생성자
    public ErrorResponseDto(String message, String code, boolean isSuccess) {
        this(null,isSuccess,code,message); // httpStatus는 기본값으로 null
    }

    public ErrorResponseDto(HttpStatus httpStatus, boolean isSuccess, String code,String message){
        this.httpStatus=httpStatus;
        this.isSuccess=isSuccess;
        this.code=code;
        this.message=message;
    }

    // Getter 메서드
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
