package uk.jinhy.survey_mate_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.response.Status;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Status status = null;

        String exception = (String)request.getAttribute("exception");


        if(exception.equals(Status.JWT_NULL.getCode())){
            status = Status.JWT_NULL;
            setResponse(response, status);
        }

        if(exception.equals(Status.JWT_INVALID.getCode())){
            status = Status.JWT_INVALID;
            setResponse(response, status);
        }

    }

    private void setResponse(HttpServletResponse response, Status status) throws IOException {
        final Map<String, Object> body = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        body.put("status" , status.getHttpStatus());
        body.put("errorCode", status.getCode());
        body.put("message", status.getMessage());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
