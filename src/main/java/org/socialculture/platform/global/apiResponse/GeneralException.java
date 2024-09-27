package org.socialculture.platform.global.apiResponse;

public class GeneralException extends RuntimeException{
    private BaseErrorCode code;
    private String sourceClass;
    private String sourceMethod;
    private String sourcePackage;
    private String sourceAddress;
}
