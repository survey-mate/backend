package uk.jinhy.survey_mate_api.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.domain.repository.MemberRepository;
import uk.jinhy.survey_mate_api.auth.presentation.dto.LoginControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MemberControllerDTO;
import uk.jinhy.survey_mate_api.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Qualifier("AuthenticationManager")
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PasswordEncoder passwordEncoder;

    public Member join(MemberControllerDTO.MemberRequestDTO requestDTO){
        Member member = Member.builder()
                .memberId(requestDTO.getMemberId())
                .nickname(requestDTO.getNickname())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .messageConsent(requestDTO.isMessageConsent())
                .marketingConsent(requestDTO.isMarketingConsent())
                .point(0L)
                .profileUrl(null)
                .build();

        memberRepository.save(member);
        return member;
    }

    public String login(LoginControllerDTO requestDTO){
        String id = requestDTO.getId();
        String password = requestDTO.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(id);
        String jwtToken = jwtTokenProvider.createToken(userDetails.getUsername());
       /* SecurityContextHolder.getContext().setAuthentication(authentication);*/

        return jwtToken;

    }


}
