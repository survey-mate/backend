package uk.jinhy.survey_mate_api.common.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

public class AuthProvider {
    public static User getAuthenticationInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(Status.NO_AUTH);
        }

        return (User) authentication.getPrincipal();
    }

    public static String getAuthenticationInfoMemberId() {
        return getAuthenticationInfo().getUsername();
    }
}
