package org.socialculture.platform.global.apiResponse;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice(annotations = {RestController.class})
//모든 RestController에 전역적 예외를 처리하겠다.
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest webRequest){
//        String errorMessage= e.getMessage();
//        logger.error("Validation error: {}",errorMessage,e);
//        return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY, request);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        logger.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
    }//특정한 예외 핸들러에서 처리되지 않은 예상치 못한 예외가 발생했을 때 이 메서드가 호출된다. -> INTERNAL_SERVER_ERROR

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto = generalException.getErrorReasonHttpStatus();
        return handleExceptionInternal(generalException, errorResponseDto, null, request);
    }//비즈니스 로직에서 발생한 예외를 다룰 때 사용

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorResponseDto response,
                                                           HttpHeaders headers, HttpServletRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(response.getHttpStatus(),response.getCode(), response.getMessage(), null);
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                response.getHttpStatus(),
                webRequest
        );
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

//    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonStatus,
//                                                               WebRequest request, Map<String, String> errorArgs) {
//        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getHttpStatus(), errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
//        return super.handleExceptionInternal(
//                e,
//                body,
//                headers,
//                errorCommonStatus.getHttpStatus(),
//                request
//        );
//    }
//    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus,
//                                                                     HttpHeaders headers, WebRequest request) {
//        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getHttpStatus(), errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
//        return super.handleExceptionInternal(
//                e,
//                body,
//                headers,
//                errorCommonStatus.getHttpStatus(),
//                request
//        );
//    }



}
