package com.auth.login.api.login.dto.oauth;

import com.auth.login.domain.user.constants.UserRole;
import com.auth.login.domain.user.entity.User;
import com.auth.login.global.constants.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Builder
@Getter
public class OAuth2Dto {

    private String attributeKey;

    private OAuth2UserInfoDto user;

    public static OAuth2Dto of(SocialType socialType,
                               String attributeKey,
                               Map<String, Object> attributes) {
        return switch (socialType) {
            case NAVER ->
                    OAuth2Dto.builder()
                            .attributeKey(attributeKey)
                            .user(new NaverOAuth2UserInfoDto(attributes))
                            .build();
            case KAKAO ->
                    OAuth2Dto.builder()
                            .attributeKey(attributeKey)
                            .user(new KaKaoOAuth2UserInfoDto(attributes))
                            .build();
            case GOOGLE ->
                    OAuth2Dto.builder()
                            .attributeKey(attributeKey)
                            .user(new GoogleOAuth2UserInfoDto(attributes))
                            .build();
        };
    }

    public User toEntity(SocialType socialType) {
        return User.builder()
                .socialType(socialType)
                .socialId(user.getId())
                .email(user.getEmail())
                .role(UserRole.GUEST)
                .build();
    }

    public static abstract class OAuth2UserInfoDto {

        protected Map<String, Object> attributes;

        public OAuth2UserInfoDto(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public abstract String getId();

        public abstract String getEmail();

    }

    public static class NaverOAuth2UserInfoDto extends OAuth2UserInfoDto {

        public NaverOAuth2UserInfoDto(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return Optional.ofNullable(response)
                    .map(r -> (String) r.get("id"))
                    .orElse(null);
        }

        @Override
        public String getEmail() {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return Optional.ofNullable(response)
                    .map(r -> (String) r.get("email"))
                    .orElse(null);
        }

    }

    public static class KaKaoOAuth2UserInfoDto extends OAuth2UserInfoDto {

        public KaKaoOAuth2UserInfoDto(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            return String.valueOf(attributes.get("id"));
        }

        @Override
        public String getEmail() {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            return Optional.ofNullable(account)
                    .map(a -> (String) a.get("email"))
                    .orElse(null);
        }

    }

    public static class GoogleOAuth2UserInfoDto extends OAuth2UserInfoDto {

        public GoogleOAuth2UserInfoDto(Map<String, Object> attributes) {
            super(attributes);
        }

        @Override
        public String getId() {
            return (String) attributes.get("sub");
        }

        @Override
        public String getEmail() {
            return (String) attributes.get("email");
        }

    }

}
