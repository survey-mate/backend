package uk.jinhy.survey_mate_api.auth.application.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthProvider authProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (isSwaggerUiRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = extractJwtToken(request.getHeader("Authorization"));
            if (jwt != null) {
                authProvider.setAuthentication(jwt);
            }
        } catch (JwtException e) {
            request.setAttribute("exception", Status.JWT_INVALID.getCode());
        } catch (Exception e) {
            request.setAttribute("exception", Status.JWT_NULL.getCode());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSwaggerUiRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs");
    }

    private String extractJwtToken(String header) {
        try {
            return header.replace("Bearer ", "").trim();
        } catch (Exception e) {
            throw new GeneralException(Status.JWT_NULL);
        }
    }
}
