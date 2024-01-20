package uk.jinhy.survey_mate_api.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.springframework.web.filter.GenericFilterBean;
import uk.jinhy.survey_mate_api.auth.util.JwtTokenUtil;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenUtil jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }
}
