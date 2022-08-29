package com.auth.login.api.login.service;

import com.auth.login.api.login.dto.oauth.OAuth2Dto;
import com.auth.login.domain.user.entity.User;
import com.auth.login.domain.user.service.UserService;
import com.auth.login.domain.user.vo.OAuth2UserVo;
import com.auth.login.global.constants.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2LoginService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User user = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();
        SocialType socialType = SocialType.findByRegistrationId(registrationId);
        String attributeKey = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = user.getAttributes();
        OAuth2Dto dto = OAuth2Dto.of(socialType, attributeKey, attributes);
        User target = findByAttributesAndSocialType(dto, socialType);

        return new OAuth2UserVo(
                Collections.singleton(
                        new SimpleGrantedAuthority(
                                target.getRole().getKey()
                        )
                ),
                attributes,
                dto.getAttributeKey(),
                target.getEmail(),
                target.getRole()
        );
    }

    public User findByAttributesAndSocialType(OAuth2Dto attributes, SocialType socialType) {
        String socialId = attributes
                .getUser()
                .getId();
        return userService.findBySocialTypeAndSocialId(socialType, socialId)
                .orElseGet(() -> save(attributes, socialType));
    }

    @Transactional
    public User save(OAuth2Dto attribute, SocialType socialType) {
        User user = attribute.toEntity(socialType);
        return userService.save(User.createUser(user));
    }

}
