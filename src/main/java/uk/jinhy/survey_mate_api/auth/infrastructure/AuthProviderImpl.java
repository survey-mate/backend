package uk.jinhy.survey_mate_api.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.auth.application.service.AuthProvider;
import uk.jinhy.survey_mate_api.common.jwt.JwtTokenProvider;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@RequiredArgsConstructor
@Component
public class AuthProviderImpl implements AuthProvider {

    @Qualifier("AuthenticationManager")
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(Status.UNAUTHENTICATED_USER);
        }

        User user = (User) authentication.getPrincipal();
        return user.getUsername();
    }

    @Override
    public String generateToken(String id, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(id, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtTokenProvider.createToken(authentication);
    }
}
