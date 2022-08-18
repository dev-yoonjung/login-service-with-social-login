package com.auth.login.global.error.exception;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(401, message);
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
