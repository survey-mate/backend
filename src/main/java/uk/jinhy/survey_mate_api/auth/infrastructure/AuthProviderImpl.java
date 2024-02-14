package uk.jinhy.survey_mate_api.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.auth.application.service.AuthProvider;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.domain.repository.MemberRepository;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@RequiredArgsConstructor
@Component
public class AuthProviderImpl implements AuthProvider {

    private final MemberRepository memberRepository;

    private static final long ACCESS_TOKEN_DURATION = 30L * 60L * 1000L;

    @Value("${jwt.secret}")
    String secretKey;

    private Key key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void setAuthentication(String accessToken) {
        Claims claims = validateTokenAndParseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
            authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Claims validateTokenAndParseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

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
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));

        String authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"))
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        LocalDateTime validity = LocalDateTime.now().plusSeconds(ACCESS_TOKEN_DURATION);
        Date expirationDate = Date.from(validity.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
            .setSubject(id)
            .claim("role", authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(expirationDate)
            .compact();
    }
}
