package com.example.myjwt.jwt.exception;

import com.example.myjwt.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TokenExceptionType implements CustomExceptionType {

    ACCESS_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "ACCESS TOKEN이 존재하지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    AUTHORITY_NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String errorMsg;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}