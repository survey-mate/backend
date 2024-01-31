package uk.jinhy.survey_mate_api.auth.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import uk.jinhy.survey_mate_api.auth.presentation.dto.AuthControllerDTO;
import uk.jinhy.survey_mate_api.common.email.service.MailService;
import uk.jinhy.survey_mate_api.common.jwt.JwtTokenProvider;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.common.util.Util;

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

    public AuthControllerDTO.MemberResponseDTO join(AuthControllerDTO.MemberRequestDTO requestDTO) {

        String emailAddress = requestDTO.getMemberId();
        String emailToken = requestDTO.getEmailToken();

        if (!emailTokenRepository.existsByEmailAddressAndToken(emailAddress, emailToken)) {
            throw new GeneralException(Status.MAIL_TOKEN_INVALID);
        }

        if (memberRepository.existsByMemberId(emailAddress)) {
            throw new GeneralException(Status.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
            .memberId(requestDTO.getMemberId())
            .nickname(requestDTO.getNickname())
            .password(passwordEncoder.encode(requestDTO.getPassword()))
            .serviceConsent(requestDTO.isServiceConsent())
            .privacyConsent(requestDTO.isPrivacyConsent())
            .point(0L)
            .profileUrl(null)
            .build();

        if (emailAddress.matches(".*\\.(ac\\.kr|edu)$")) {
            member.setIsStudent(true);
        } else {
            member.setIsStudent(false);
        }

        memberRepository.save(member);

        return AuthControllerDTO.MemberResponseDTO.builder()
            .id(member.getMemberId())
            .build();
    }

    public AuthControllerDTO.JwtResponseDTO login(AuthControllerDTO.LoginRequestDTO requestDTO) {
        String id = requestDTO.getId();
        String password = requestDTO.getPassword();

        if (!memberRepository.existsByMemberId(id)) {
            throw new GeneralException(Status.MEMBER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(id, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String jwtToken = jwtTokenProvider.createToken(authentication);

        return AuthControllerDTO.JwtResponseDTO.builder()
            .jwt(jwtToken)
            .build();
    }

    public String sendMailCode(AuthControllerDTO.CertificateCodeRequestDTO requestDTO) {
        String memberId = requestDTO.getReceiver();
        if (memberRepository.existsByMemberId(memberId)) {
            throw new GeneralException(Status.DUPLICATE_MAIL);
        }

        String mailValidationCode = Util.generateRandomNumberString(6);
        MailCode mailCode = MailCode.builder()
            .code(mailValidationCode)
            .emailAddress(requestDTO.getReceiver())
            .createdAt(LocalDateTime.now())
            .build();
        mailCodeRepository.save(mailCode);
        return mailValidationCode;
    }

    public AuthControllerDTO.EmailCodeResponseDTO checkEmailCode(
        AuthControllerDTO.MailCodeRequestDTO mailCodeDto) {
        String id = mailCodeDto.getEmailAddress();
        String mailValidationCode = mailCodeDto.getCode();

        MailCode mailCode = mailCodeRepository.findByCodeAndEmailAddress(mailValidationCode, id)
            .orElseThrow(() -> new GeneralException(Status.MAIL_CODE_DIFFERENT));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = mailCode.getCreatedAt().plusMinutes(3);
        if (currentTime.isAfter(expirationTime)) {
            throw new GeneralException(Status.MAIL_CODE_TIME_OUT);
        }

        String accountValidationToken = Util.generateRandomString(10);

        EmailToken emailToken = EmailToken.builder()
            .token(accountValidationToken)
            .emailAddress(id)
            .build();

        emailTokenRepository.save(emailToken);

        return AuthControllerDTO.EmailCodeResponseDTO.builder()
            .emailValidationToken(accountValidationToken)
            .build();
    }

    public String sendPasswordResetCode(AuthControllerDTO.CertificateCodeRequestDTO requestDTO) {
        String memberId = requestDTO.getReceiver();
        Member member = getMemberById(memberId);

        String accountValidationCode = Util.generateRandomNumberString(6);
        PasswordResetCode passwordResetCode = PasswordResetCode.builder()
            .code(accountValidationCode)
            .emailAddress(memberId)
            .createdAt(LocalDateTime.now())
            .build();

        passwordResetCodeRepository.save(passwordResetCode);
        return accountValidationCode;
    }

    public AuthControllerDTO.PasswordResetCodeResponseDTO checkPasswordResetCode(
        AuthControllerDTO.PasswordResetCodeRequestDTO resetDTO) {
        String id = resetDTO.getEmailAddress();
        String code = resetDTO.getCode();

        PasswordResetCode resetCode = passwordResetCodeRepository.findByCodeAndEmailAddress(code,
                id)
            .orElseThrow(() -> new GeneralException(Status.PASSWORD_RESET_CODE_DIFFERENT));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = resetCode.getCreatedAt().plusMinutes(3);
        if (currentTime.isAfter(expirationTime)) {
            throw new GeneralException(Status.PASSWORD_RESET_CODE_TIME_OUT);
        }

        String passwordRestValidationToken = Util.generateRandomNumberString(10);

        PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(passwordRestValidationToken)
            .emailAddress(id)
            .build();

        passwordResetTokenRepository.save(resetToken);

        return AuthControllerDTO.PasswordResetCodeResponseDTO.builder()
            .passwordRestValidationToken(passwordRestValidationToken)
            .build();
    }

    public void resetPassword(AuthControllerDTO.PasswordResetRequestDTO requestDto) {
        String emailAddress = requestDto.getEmailAddress();
        Member member = memberRepository.findById(emailAddress)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));

        String resetToken = requestDto.getPasswordResetToken();

        if (!passwordResetTokenRepository.existsByEmailAddressAndToken(emailAddress, resetToken)) {
            throw new GeneralException(Status.PASSWORD_TOKEN_INVALID);
        }

        member.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));

        memberRepository.save(member);
    }

    public void updatePassword(AuthControllerDTO.PasswordUpdateRequestDTO requestDto) {
        Member member = getCurrentMember();
        String currentPassword = requestDto.getCurrentPassword();

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new GeneralException(Status.CURRENT_PASSWORD_INCORRECT);
        }

        member.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));

        memberRepository.save(member);
    }

    public void deleteAccount(AuthControllerDTO.DeleteAccountRequestDTO requestDTO) {
        Member member = getCurrentMember();

        log.info(requestDTO.getCurrentPassword());
        String emailAddress = member.getMemberId();
        if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), member.getPassword())) {
            throw new GeneralException(Status.PASSWORD_INCORRECT);
        }

        memberRepository.deleteById(emailAddress);
    }

    public boolean isStudentAccount() {
        return getCurrentMember().isStudent();
    }

    public Member getCurrentMember() {
        String memberId = AuthProvider.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

}
