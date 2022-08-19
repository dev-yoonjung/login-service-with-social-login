package com.auth.login.api.login.service;

import com.auth.login.api.login.dto.SignUpDto;
import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;

    public SignUpDto.Response signUp(@RequestBody SignUpDto.Request request) {
        User user = User.createUser(request.toEntity());
        User saved = userService.save(user);

        return SignUpDto.Response.of(saved);
    }

}
