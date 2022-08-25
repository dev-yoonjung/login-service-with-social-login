package com.auth.login.global.handler;

import com.auth.login.domain.jwt.dto.TokenDto;
import com.auth.login.domain.jwt.service.TokenManager;
import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.service.UserService;
import com.auth.login.domain.user.vo.UserVo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenManager tokenManager;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication) {
        User user = ((UserVo) authentication.getPrincipal()).getUser();
        String email = user.getEmail();
        TokenDto tokenDto = tokenManager.createTokenDto(email, user.getRole());

        User target = userService.findByEmail(email);
        target.updateRefreshToken(tokenDto.getRefreshToken());

        response.addHeader(HttpHeaders.AUTHORIZATION, tokenDto.getAccessToken());
        response.addHeader(String.format("%s-Refresh", HttpHeaders.AUTHORIZATION), tokenDto.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
