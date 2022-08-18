package com.auth.login.global.error.exception;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

}
