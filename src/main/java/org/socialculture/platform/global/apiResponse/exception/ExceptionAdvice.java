package org.socialculture.platform.global.apiResponse.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
//모든 RestController에 전역적 예외를 처리하겠다.
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);


    // 일반 예외 핸들링 (체크되지 않은 예외 포함)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> exception(Exception e) {
        logger.error("Unhandled exception occurred: {}", e.getMessage(), e);
        ApiResponse<Object> body = ApiResponse.onFailure(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "처리되지 않은 오류가 발생했습니다.",
                null
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 언체크 예외 핸들링
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> onThrowException(GeneralException generalException) {
        ApiResponse<Object> body = ApiResponse.onFailure(
                generalException.getErrorReasonHttpStatus().getHttpStatus(),
                generalException.getErrorReasonHttpStatus().getCode(),
                generalException.getErrorReasonHttpStatus().getMessage(),
                null
        );
        return new ResponseEntity<>(body, generalException.getErrorReasonHttpStatus().getHttpStatus());
    }



    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getHttpStatus(), errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }//일반적인 예외와 더불어 에러 발생 지점(errorPoint)에 대한 정보를 포함한 응답을 반환





}
