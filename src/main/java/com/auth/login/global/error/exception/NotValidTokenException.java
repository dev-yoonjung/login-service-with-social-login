package com.auth.login.global.error.exception;

public class NotValidTokenException extends BusinessException {

    public NotValidTokenException(ErrorCode error) {
        super(error);
    }

}
