package com.auth.login.global.error.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {

    public CustomAuthenticationException(String message) {
        super(message);
    }

    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

}
