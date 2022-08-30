package com.auth.login.global.handler;

import com.auth.login.domain.jwt.dto.TokenDto;
import com.auth.login.domain.jwt.service.TokenManager;
import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.service.UserService;
import com.auth.login.domain.user.vo.OAuth2UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenManager tokenManager;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        OAuth2UserVo user = (OAuth2UserVo) authentication.getPrincipal();
        String email = user.getEmail();
        UserRole role = user.getRole();

        if (role == UserRole.GUEST) {
            Date accessTokenExpireTime = tokenManager.createAccessTokenExpireTime();
            String accessToken = tokenManager.createAccessToken(email, role, accessTokenExpireTime);

            response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("oauth2/sign-up");
            return;
        }

        TokenDto tokenDto = tokenManager.createTokenDto(email, role);

        User target = userService.findByEmail(email);
        target.updateRefreshToken(tokenDto.getRefreshToken());

        response.addHeader(HttpHeaders.AUTHORIZATION, tokenDto.getAccessToken());
        response.addHeader(String.format("%s-Refresh", HttpHeaders.AUTHORIZATION), tokenDto.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
