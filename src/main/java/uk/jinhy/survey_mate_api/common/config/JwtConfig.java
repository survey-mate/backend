package uk.jinhy.survey_mate_api.common.config;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String plainSecretKey;


    @Bean
    public SecretKey getSecretKey() {
        String keyBase64Encoded =
            Base64.getEncoder().encodeToString(plainSecretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
}
