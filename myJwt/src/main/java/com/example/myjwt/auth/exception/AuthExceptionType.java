package com.example.myjwt.auth.exception;

import com.example.myjwt.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthExceptionType implements CustomExceptionType {

    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "가입되지 않은 이메일이거나 비밀번호가 일치하지 않습니다."),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "삭제된 계정입니다."),
    ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "계정이 잠겨있습니다.");

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