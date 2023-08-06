package com.example.myjwt.jwt.exception;

import com.example.myjwt.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenException extends CustomException {

    private final TokenExceptionType tokenExceptionType;

    @Override
    public TokenExceptionType getCustomExceptionType() {
        return tokenExceptionType;
    }
}
