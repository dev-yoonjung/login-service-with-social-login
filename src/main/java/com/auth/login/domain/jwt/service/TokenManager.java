package com.auth.login.domain.jwt.service;

import com.auth.login.domain.jwt.constants.GrantType;
import com.auth.login.domain.jwt.constants.TokenType;
import com.auth.login.domain.jwt.dto.TokenDto;
import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.global.error.exception.ErrorCode;
import com.auth.login.global.error.exception.NotValidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class TokenManager {

    private final Key key;

    @Value("${token.access.expiration_time}")
    private Long accessTokenExpirationTime;

    @Value("${token.refresh.expiration_time}")
    private  Long refreshTokenExpirationTime;

    @Autowired
    public TokenManager(@Value("${token.secret}") String secret) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createTokenDto(String email, UserRole role) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, role, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);

        return TokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + accessTokenExpirationTime);
    }

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + refreshTokenExpirationTime);
    }

    public String createAccessToken(String email, UserRole role, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())
                .setAudience(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("role", role)
                .signWith(key)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public String createRefreshToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())
                .setAudience(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(key)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public String getUserEmail(String accessToken) {
        String email;

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            email = claims.getAudience();
        } catch (Exception e){
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        return email;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch(JwtException e) {
            log.info("잘못된 토큰입니다: ", e);
        } catch (Exception e){
            log.info("jwt token 검증 중 에러가 발생했습니다: ", e);
        }

        return false;
    }

    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        return claims;
    }

    public boolean isTokenExpired(Date tokenExpiredTime) {
        Date now = new Date();
        return now.after(tokenExpiredTime);
    }

    public String getTokenType(String token) {
        String tokenType;

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            tokenType = claims.getSubject();
        } catch (Exception e){
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        return tokenType;
    }

    public String getRole(String token) {
        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e){
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }

        return (String) claims.get("role");
    }

}
