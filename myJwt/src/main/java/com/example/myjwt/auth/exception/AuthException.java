package com.example.myjwt.auth.exception;

import com.example.myjwt.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthException extends CustomException {

    private final AuthExceptionType authExceptionType;

    @Override
    public AuthExceptionType getCustomExceptionType() {
        return authExceptionType;
    }
}