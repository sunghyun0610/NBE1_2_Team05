package org.socialculture.platform.global.apiResponse;

public interface BaseCode {
    ResponseDto getResponseDetails();//메서지, 코드, 결과
    ResponseDto getResponseWithHttpStatus();//메세지 ,코드 ,결과, http상태코드까지
}
