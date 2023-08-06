package com.example.myjwt.global.exception;

public abstract class CustomException extends RuntimeException {

    public abstract CustomExceptionType getCustomExceptionType();

}
