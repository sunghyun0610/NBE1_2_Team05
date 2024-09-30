package org.socialculture.platform.global.apiResponse.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode{
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 관련 에러
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "MEMBER4001", "로그인에 실패했습니다."),
    REGISTER_FAIL(HttpStatus.UNAUTHORIZED, "MEMBER4004", "회원가입에 실패했습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "MEMBER4091", "이미 사용중인 이메일 입니다."),
    EMAIL_INVALID(HttpStatus.BAD_REQUEST, "MEMBER4002", "이메일 형식이 맞지 않습니다."),
    NAME_DUPLICATE(HttpStatus.CONFLICT, "MEMBER4092", "이미 사용중인 닉네임 입니다."),
    NAME_INVALID(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임 형식이 올바르지 않습니다."),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "MEMBER4003", "비밀번호 형식이 맞지 않습니다."),


    // 소셜 유저 관련 에러
    SOCIAL_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "SOCIAL4091", "이메일이 이미 다른 소셜 계정에서 사용중입니다."),
    SOCIAL_NAME_INVALID(HttpStatus.BAD_REQUEST, "SOCIAL4002", "닉네임 형식이 올바르지 않습니다."),
    SOCIAL_NAME_DUPLICATE(HttpStatus.CONFLICT, "SOCIAL4092", "닉네임이 이미 사용 중입니다."),


    ;

    //공연

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



    private ErrorStatus(HttpStatus httpStatus, String code, String message){
        this.httpStatus=httpStatus;
        this.code=code;
        this.message=message;
    }

    @Override
    public ErrorResponseDto getResponseWithHttpStatus() {
        return new ErrorResponseDto(httpStatus,false,code,message);
    }//상태코드 실패여부 메세지까지 전달
}
