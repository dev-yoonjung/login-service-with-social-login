package com.auth.login.domain.user.repository;

import com.auth.login.domain.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
