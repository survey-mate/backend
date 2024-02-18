package uk.jinhy.survey_mate_api.common.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uk.jinhy.survey_mate_api.auth.infrastructure.AuthenticationProviderImpl;
import uk.jinhy.survey_mate_api.auth.presentation.JwtAccessDeniedHandler;
import uk.jinhy.survey_mate_api.auth.presentation.JwtAuthenticationEntryPoint;
import uk.jinhy.survey_mate_api.auth.presentation.JwtAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String[] allowedUrls = {
        "/",
        "/swagger-ui/**",
        "/v3/**",
        "/api/auth/login",
        "/api/auth/join",
        "/api/auth/email/**",
        "/api/auth/password/certification",
        "/api/auth/password/certification-request",
        "/api/auth/password/reset",
        "/api/auth/nickname/*",
        "/error",
        "/v2/api-docs",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/configuration/ui",
        "/configuration/security",
        "/webjars/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderImpl(userDetailsService, passwordEncoder());
    }

    @Bean(name = "AuthenticationManager")
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .authorizeHttpRequests(
                auth -> {
                    auth
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().hasRole("USER");
                }
            )
            .cors(
                cors -> cors.configurationSource(
                    corsConfigurationSource()
                )
            )
            .csrf(CsrfConfigurer::disable)
            .headers(httpSecurityHeadersConfigurer ->
                httpSecurityHeadersConfigurer.frameOptions(
                    HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(
                exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            );

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
