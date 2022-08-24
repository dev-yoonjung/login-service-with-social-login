package com.auth.login.api.login.service;

import com.auth.login.api.login.dto.SignUpDto;
import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.service.UserService;
import com.auth.login.domain.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService implements UserDetailsService {

    private final UserService userService;

    @Transactional
    public SignUpDto.Response signUp(@RequestBody SignUpDto.Request request) {
        User user = User.createUser(request.toEntity());
        User saved = userService.save(user);

        return SignUpDto.Response.of(saved);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        return new UserVo(user);
    }
}
