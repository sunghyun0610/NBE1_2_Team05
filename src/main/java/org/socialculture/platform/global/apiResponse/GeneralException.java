package org.socialculture.platform.global.apiResponse;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{// 체크되지 않은 예외
    private BaseErrorCode code;//사용자 정의한 예외 코드
    private String sourceClass;//예외 발생한 클래스이름
    private String sourceMethod;//예외 발생한 메서드이름
    private String sourcePackage;
    private String sourceAddress;

    public GeneralException(BaseErrorCode errorCode) {
        extractSourceInfo();
        this.code = errorCode;
    }

    public ErrorResponseDto getErrorReasonHttpStatus(){
        return this.code.getResponseWithHttpStatus();
    }

    private void extractSourceInfo() {
        StackTraceElement[] stackTrace = this.getStackTrace();
        if (stackTrace.length > 1) {
            StackTraceElement element = stackTrace[1];  // [0]는 현재 생성자, [1]은 CustomException을 발생시킨 메서드
            this.sourceClass = element.getClassName();
            this.sourceMethod = element.getMethodName();

            // 패키지 정보는 클래스 이름에서 추출
            int lastDotIndex = this.sourceClass.lastIndexOf('.');
            if (lastDotIndex > 0) {
                this.sourcePackage = this.sourceClass.substring(0, lastDotIndex);
            } else {
                this.sourcePackage = "Unknown";
            }
        }
        this.sourceAddress = sourcePackage + "." + sourceClass + "." + sourceMethod;
    }
}
