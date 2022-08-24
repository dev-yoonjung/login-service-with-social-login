package com.auth.login.domain.user.service;

import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.repository.UserRepository;
import com.auth.login.global.error.exception.BusinessException;
import com.auth.login.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_ACCOUNT));
    }

}
