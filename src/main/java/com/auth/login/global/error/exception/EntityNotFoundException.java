package com.auth.login.global.error.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(400, message);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
