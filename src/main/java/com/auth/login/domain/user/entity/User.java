package com.auth.login.domain.user.entity;

import com.auth.login.domain.base.BaseEntity;
import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.global.constants.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User createUser(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .socialType(user.getSocialType())
                .socialId(user.getSocialId())
                .refreshToken(user.getRefreshToken())
                .build();
    }

}
