package com.auth.login.domain.jwt.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GrantType {

    BEARER("Bearer");

    private final String type;

}
