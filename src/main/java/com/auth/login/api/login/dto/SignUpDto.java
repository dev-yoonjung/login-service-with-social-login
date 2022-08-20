package com.auth.login.api.login.dto;

import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.domain.user.entity.User;
import com.auth.login.global.constants.SocialType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class SignUpDto {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Getter
    @NoArgsConstructor
    public static class Request {

        @NotBlank
        private String email;

        @NotBlank
        private String password;

        private SocialType socialType;

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .role(UserRole.GUEST)
                    .socialType(socialType)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class Response {

        private String email;

        private UserRole role;

        private SocialType socialType;

        private Instant createTime;

        public static Response of(User user) {
            return Response.builder()
                    .email(user.getEmail())
                    .role(user.getRole())
                    .socialType(user.getSocialType())
                    .createTime(
                            user.getCreateTime().toInstant(ZoneOffset.of("Asia/Seoul"))
                    )
                    .build();
        }

    }

}
