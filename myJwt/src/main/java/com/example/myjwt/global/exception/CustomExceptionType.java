package com.example.myjwt.global.exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionType {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
