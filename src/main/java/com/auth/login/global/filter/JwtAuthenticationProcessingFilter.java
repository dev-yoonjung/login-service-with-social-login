package com.auth.login.global.filter;

import com.auth.login.domain.jwt.constants.GrantType;
import com.auth.login.domain.jwt.constants.TokenType;
import com.auth.login.domain.jwt.service.TokenManager;
import com.auth.login.global.error.exception.CustomAuthenticationException;
import com.auth.login.global.error.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 1. 토큰 유무 확인
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authorizationHeader)) {
            throw new CustomAuthenticationException(ErrorCode.NOT_EXISTS_AUTHORIZATION);
        }

        // 2. Bearer Grant Type 확인
        String[] authorizations = authorizationHeader.split(" ");
        if(authorizations.length < 2 || !GrantType.BEARER.getType().equals(authorizations[0])) {
            throw new CustomAuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
        }

        String token = authorizations[1];

        // 3. 토큰 검증
        if(!tokenManager.validateToken(token)) {
            throw new CustomAuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }

        String tokenType = tokenManager.getTokenType(token);

        // 4. 토큰 타입 검증
        if(!TokenType.ACCESS.name().equals(tokenType)) {
            throw new CustomAuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 5. 엑세스 토큰 만료 시간 검증
        Claims tokenClaims = tokenManager.getTokenClaims(token);
        Date expiration = tokenClaims.getExpiration();
        if(tokenManager.isTokenExpired(expiration)) {
            throw new CustomAuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
    }
}
