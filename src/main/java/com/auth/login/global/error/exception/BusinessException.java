package com.auth.login.global.error.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private int status;

    public BusinessException(int status, String message) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorCode error) {
        super(error.getMessage());
        this.status = error.getStatus();
    }

}
