package uk.jinhy.survey_mate_api.auth.application.service;

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
import uk.jinhy.survey_mate_api.auth.domain.entity.PasswordResetCode;
import uk.jinhy.survey_mate_api.auth.domain.entity.PasswordResetToken;
import uk.jinhy.survey_mate_api.auth.domain.repository.EmailTokenRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.MailCodeRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.MemberRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.PasswordResetCodeRepository;
import uk.jinhy.survey_mate_api.auth.domain.repository.PasswordResetTokenRepository;
import uk.jinhy.survey_mate_api.auth.presentation.dto.LoginControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MailCodeControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.CertificateCodeRequestDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.MemberControllerDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.PasswordResetCodeDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.PasswordResetControllerDTO;
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

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

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

    public String sendMailCode(CertificateCodeRequestDTO requestDTO){
        String memberId = requestDTO.getReceiver();
        boolean isExist = memberRepository.existsByMemberId(memberId);
        if(isExist){
            throw new GeneralException(Status.DUPLICATE_MAIL);
        }

        requestDTO.setMailSubject("!썰매! 회원가입 전 학교 이메일을 인증해주세요. 이메일 인증 코드 전송");
        requestDTO.setTitle("학교 이메일 확인용 인증코드");

        String code = createCode();
        MailCode mailCode = MailCode.builder()
                .code(code)
                .emailAddr(requestDTO.getReceiver())
                .build();
        mailCodeRepository.save(mailCode);
        mailService.sendEmail(requestDTO, code);
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

    public String sendPasswordResetCode(CertificateCodeRequestDTO requestDTO){
        String memberId = requestDTO.getReceiver();
        Member member = getMemberById(memberId);

        requestDTO.setMailSubject("!썰매! 비밀번호를 잊으셨나요? 비밀번호 재설정을 도와드리겠습니다. 계정 인증 코드 전송");
        requestDTO.setTitle("계정 확인용 인증코드");

        String code = createCode();
        PasswordResetCode resetCode = PasswordResetCode.builder()
                .code(code)
                .emailAddr(memberId)
                .build();

        passwordResetCodeRepository.save(resetCode);
        mailService.sendEmail(requestDTO, code);

        return "비밀번호 재설정 이메일 전송 성공";
    }

    public String checkPasswordResetCode(PasswordResetCodeDTO resetDTO){
        String id = resetDTO.getEmailAddr();
        String code = resetDTO.getCode();

        PasswordResetCode resetCode = passwordResetCodeRepository.findByCodeAndEmailAddr(code, id)
                .orElseThrow(() -> new GeneralException(Status.PASSWORD_RESET_CODE_DIFFERENT));

        LocalDateTime currTime = LocalDateTime.now();
        LocalDateTime expirationTime = resetCode.getCreatedAt().plusMinutes(3);
        if(currTime.isAfter(expirationTime)){
            throw new GeneralException(Status.PASSWORD_RESET_CODE_TIME_OUT);
        }

        String token = createRandomStr();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .emailAddr(id)
                .build();

        passwordResetTokenRepository.save(resetToken);

        return token;
    }

    public String resetPassword(PasswordResetControllerDTO requestDto){
        log.info("resetPassword");
        Member member = getCurrentMember();
        log.info(member.getMemberId());
        String emailAddr = member.getMemberId();
        String resetToken = requestDto.getPasswordResetToken();

        if(!passwordResetTokenRepository.existsByEmailAddrAndToken(emailAddr, resetToken)){
            throw new GeneralException(Status.PASSWORD_TOKEN_INVALID);
        }

        Member modifiedMember = Member.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .password(passwordEncoder.encode(requestDto.getNewPassword()))
                .messageConsent(member.isMessageConsent())
                .marketingConsent(member.isMarketingConsent())
                .point(member.getPoint())
                .profileUrl(member.getProfileUrl())
                .build();

        memberRepository.save(modifiedMember);
        return modifiedMember.getMemberId();
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

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
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
