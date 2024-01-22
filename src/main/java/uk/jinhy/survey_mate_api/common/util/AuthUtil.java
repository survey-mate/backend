package uk.jinhy.survey_mate_api.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuthUtil {
    public static User getAuthenticationInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증정보가 없습니다.");
        }
        return (User) authentication.getPrincipal();
    }

    public static String getAuthenticationInfoMemberId() {
        return getAuthenticationInfo().getUsername();
    }
}
