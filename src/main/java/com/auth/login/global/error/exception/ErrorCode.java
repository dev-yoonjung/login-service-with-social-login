package com.auth.login.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_REGISTERED_MEMBER(400, "이미 가입된 회원입니다."),
    INVALID_SOCIAL_TYPE(400, "잘못된 소셜 로그인 타입입니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_EXIST_ACCOUNT(400, "존재하지 않는 회원입니다."),
    LOGIN_ERROR(401, "아이디 또는 비밀번를 입력해주세요."),
    NOT_EXISTS_AUTHORIZATION(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    NOT_VALID_TOKEN(401, "유효하지않은 토큰 입니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "토큰 타입이 access token이 아닙니다."),
    REFRESH_TOKEN_EXPIRED(401, "해당 refresh token은 만료됐습니다."),
    FORBIDDEN_ROLE(403, "회원 권한이 없습니다.");

    private final int status;

    private final String message;

}
