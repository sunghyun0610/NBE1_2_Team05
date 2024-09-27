package org.socialculture.platform.global.apiResponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode{
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "금지된 요청입니다."),;


    private final HttpStatus httpStatus;
//    private final String code;
    private final String message;

    private ErrorStatus(HttpStatus httpStatus, String message){
        this.httpStatus=httpStatus;
        this.message=message;
    }


    @Override
    public ErrorResponseDto getResponseDetails() {
        return new ErrorResponseDto(message,false);
    }

    @Override
    public ErrorResponseDto getResponseWithHttpStatus() {
        return new ErrorResponseDto(httpStatus,false,message);
    }
}
