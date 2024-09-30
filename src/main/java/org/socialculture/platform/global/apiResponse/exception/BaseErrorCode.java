package org.socialculture.platform.global.apiResponse.exception;

public interface BaseErrorCode {
     ErrorResponseDto getResponseWithHttpStatus();//메세지  ,결과, http상태코드까지
}
