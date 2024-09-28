package org.socialculture.platform.global.apiResponse.success;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder//매개변수 4개 이상이므로 builder pattern사용 하겠음.
@Getter
public class ResponseDto {

    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
//사실상 필요없을 것같음.
