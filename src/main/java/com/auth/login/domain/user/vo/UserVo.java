package com.auth.login.domain.user.vo;

import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserVo implements UserDetails {

    private final User user;
    private final Set<GrantedAuthority> authorities = new HashSet<>();

    public UserVo(User user) {
        this.user = user;
        authorities.add(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
