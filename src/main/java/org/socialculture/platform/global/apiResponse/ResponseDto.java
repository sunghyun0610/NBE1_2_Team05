package org.socialculture.platform.global.apiResponse;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder//매개변수 4개 이상이므로 builder pattern사용 하겠음.
public class ResponseDto {

    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;

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
