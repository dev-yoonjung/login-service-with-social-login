package com.auth.login.domain.user.repository;

import com.auth.login.domain.user.entity.User;
import com.auth.login.global.constants.SocialType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
