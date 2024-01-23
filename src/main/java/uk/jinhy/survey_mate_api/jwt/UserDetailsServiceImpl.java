package uk.jinhy.survey_mate_api.jwt;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.domain.repository.MemberRepository;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) {
        log.info("loadUserByUsername");
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
        log.info(member.getMemberId());

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(member.getMemberId(), member.getPassword(), authorities);
    }
}
