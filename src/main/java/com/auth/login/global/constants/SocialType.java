package com.auth.login.global.constants;

import com.auth.login.global.error.exception.BusinessException;
import com.auth.login.global.error.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

public enum SocialType {

    KAKAO, NAVER, GOOGLE;

    public static SocialType findByRegistrationId(String registrationId) {
        for (SocialType socialType : SocialType.values()) {
            if (StringUtils.lowerCase(socialType.name()).equals(registrationId)) {
                return socialType;
            }
        }

        throw new BusinessException(ErrorCode.INVALID_SOCIAL_TYPE);
    }

}
