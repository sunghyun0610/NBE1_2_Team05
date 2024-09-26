package org.socialculture.platform.global.apiResponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    // 생성자
    private SuccessStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }


    @Override
    public ResponseDto getResponseDetails() {
        ResponseDto responseDto = ResponseDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
        return responseDto;
    }

    @Override
    public ResponseDto getResponseWithHttpStatus() {
        return ResponseDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
