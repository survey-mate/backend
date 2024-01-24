package uk.jinhy.survey_mate_api.auth.application.service;

import com.amazonaws.services.kms.model.NotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.EmailToken;
import uk.jinhy.survey_mate_api.auth.domain.entity.MailCode;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.auth.domain.repository.EmailTokenRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.MailCodeRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.MemberRepository;
import uk.jinhy.survey_mate_api.auth.presentation.dto.LoginControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailCodeControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MemberControllerDTO;
import uk.jinhy.survey_mate_api.common.auth.AuthProvider;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    @Qualifier("AuthenticationManager")
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final MailCodeRepository mailCodeRepository;

    private final EmailTokenRepository emailTokenRepository;

    public Member join(MemberControllerDTO.MemberRequestDTO requestDTO){

        String emailAddr = requestDTO.getMemberId();
        String emailToken = requestDTO.getEmailToken();

        if(!emailTokenRepository.existsByEmailAddrAndToken(emailAddr, emailToken)){
            throw new GeneralException(Status.MAIL_TOKEN_INVALID);
        }

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
        String jwtToken = jwtTokenProvider.createToken(authentication);

        return jwtToken;

    }

    public String sendMailCode(MailControllerDTO mailDto){
        String memberId = mailDto.getReceiver();
        boolean isExist = memberRepository.existsByMemberId(memberId);
        if(isExist){
            throw new GeneralException(Status.DUPLICATE_MAIL);
        }

        String code = createEmailCode();
        MailCode mailCode = MailCode.builder()
                .code(code)
                .emailAddr(mailDto.getReceiver())
                .build();
        mailCodeRepository.save(mailCode);
        mailService.sendEmail(mailDto, code);
        return "인증 이메일 전송 성공";
    }

    public String checkEmailCode(MailCodeControllerDTO mailCodeDto){
        String id = mailCodeDto.getEmailAddr();
        String code = mailCodeDto.getCode();

        MailCode mailCode = mailCodeRepository.findByCodeAndEmailAddr(code, id)
                .orElseThrow(() -> new GeneralException(Status.MAIL_CODE_DIFFERENT));

        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime expirationTime = mailCode.getCreatedAt().plusMinutes(3);
        if(currTime.isAfter(expirationTime)){
            throw new GeneralException(Status.MAIL_CODE_TIME_OUT);
        }

        String token = createRandomStr();

        EmailToken emailToken = EmailToken.builder()
                .token(token)
                .emailAddr(id)
                .build();

        emailTokenRepository.save(emailToken);

        return token;
    }

    public Member getCurrentMember() {
        String memberId = AuthProvider.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    public Member getMemberById(String id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    private String createEmailCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new GeneralException(Status.NO_SUCH_ALGORITHM);
        }
    }

    private String createRandomStr() {
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(10, useLetters, useNumbers);

        return randomStr;
    }

}
