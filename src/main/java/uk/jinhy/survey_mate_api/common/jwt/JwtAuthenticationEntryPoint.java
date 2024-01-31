package uk.jinhy.survey_mate_api.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.response.Status;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        String exception = (String) request.getAttribute("exception");

        if (exception.equals(Status.JWT_NULL.getCode())) {
            setResponse(response, Status.JWT_NULL);
        }

        if (exception.equals(Status.JWT_INVALID.getCode())) {
            setResponse(response, Status.JWT_INVALID);
        }
    }

    private void setResponse(HttpServletResponse response, Status status) throws IOException {
        final Map<String, Object> body = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        body.put("status", status.getHttpStatus());
        body.put("errorCode", status.getCode());
        body.put("message", status.getMessage());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
