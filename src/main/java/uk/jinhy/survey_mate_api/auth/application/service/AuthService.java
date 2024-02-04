package uk.jinhy.survey_mate_api.auth.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.application.dto.AuthServiceDTO;
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

    public AuthControllerDTO.MemberResponseDTO join(AuthServiceDTO.MemberDTO dto) {

        String emailAddress = dto.getMemberId();
        String emailToken = dto.getEmailToken();

        if (!emailTokenRepository.existsByEmailAddressAndToken(emailAddress, emailToken)) {
            throw new GeneralException(Status.MAIL_TOKEN_INVALID);
        }

        if (memberRepository.existsByMemberId(emailAddress)) {
            throw new GeneralException(Status.MEMBER_ALREADY_EXIST);
        }

        String nickname = dto.getNickname();

        if (memberRepository.existsByNickname(nickname)) {
            throw new GeneralException(Status.NICKNAME_ALREADY_EXIST);
        }

        Member member = Member.builder()
            .memberId(dto.getMemberId())
            .nickname(dto.getNickname())
            .password(passwordEncoder.encode(dto.getPassword()))
            .serviceConsent(dto.isServiceConsent())
            .privacyConsent(dto.isPrivacyConsent())
            .point(0L)
            .profileUrl(null)
            .build();

        member.setIsStudent(emailAddress);

        memberRepository.save(member);

        return AuthControllerDTO.MemberResponseDTO.builder()
            .id(member.getMemberId())
            .build();
    }

    public AuthControllerDTO.JwtResponseDTO login(AuthServiceDTO.LoginDTO dto) {
        String id = dto.getId();
        String password = dto.getPassword();

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

    public String sendMailCode(AuthServiceDTO.CertificateCodeDTO dto) {
        String memberId = dto.getReceiver();
        if (memberRepository.existsByMemberId(memberId)) {
            throw new GeneralException(Status.DUPLICATE_MAIL);
        }

        String mailValidationCode = Util.generateRandomNumberString(6);
        MailCode mailCode = MailCode.builder()
            .code(mailValidationCode)
            .emailAddress(dto.getReceiver())
            .createdAt(LocalDateTime.now())
            .build();
        mailCodeRepository.save(mailCode);
        return mailValidationCode;
    }

    public AuthControllerDTO.EmailCodeResponseDTO checkEmailCode(AuthServiceDTO.MailCodeDTO dto) {
        String id = dto.getEmailAddress();
        String mailValidationCode = dto.getCode();

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

    public String sendPasswordResetCode(AuthServiceDTO.CertificateCodeDTO dto) {
        String memberId = dto.getReceiver();
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
        AuthServiceDTO.PasswordResetCodeDTO dto) {
        String id = dto.getEmailAddress();
        String code = dto.getCode();

        PasswordResetCode resetCode = passwordResetCodeRepository.findByCodeAndEmailAddress(code,
                id)
            .orElseThrow(() -> new GeneralException(Status.PASSWORD_RESET_CODE_DIFFERENT));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = resetCode.getCreatedAt().plusMinutes(3);
        if (currentTime.isAfter(expirationTime)) {
            throw new GeneralException(Status.PASSWORD_RESET_CODE_TIME_OUT);
        }

        String passwordRestValidationToken = Util.generateRandomString(10);

        PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(passwordRestValidationToken)
            .emailAddress(id)
            .build();

        passwordResetTokenRepository.save(resetToken);

        return AuthControllerDTO.PasswordResetCodeResponseDTO.builder()
            .passwordRestValidationToken(passwordRestValidationToken)
            .build();
    }

    public void resetPassword(AuthServiceDTO.PasswordResetDTO dto) {
        String emailAddress = dto.getEmailAddress();
        Member member = memberRepository.findById(emailAddress)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));

        String resetToken = dto.getPasswordResetToken();

        if (!passwordResetTokenRepository.existsByEmailAddressAndToken(emailAddress, resetToken)) {
            throw new GeneralException(Status.PASSWORD_TOKEN_INVALID);
        }

        member.changePassword(passwordEncoder.encode(dto.getNewPassword()));

        memberRepository.save(member);
    }

    public void updatePassword(AuthServiceDTO.PasswordUpdateDTO dto) {
        Member member = getCurrentMember();
        String currentPassword = dto.getCurrentPassword();

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new GeneralException(Status.CURRENT_PASSWORD_INCORRECT);
        }

        member.changePassword(passwordEncoder.encode(dto.getNewPassword()));

        memberRepository.save(member);
    }

    public void deleteAccount(AuthServiceDTO.DeleteAccountDTO dto) {
        Member member = getCurrentMember();

        String emailAddress = member.getMemberId();
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new GeneralException(Status.PASSWORD_INCORRECT);
        }

        memberRepository.deleteById(emailAddress);
    }

    public boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return true;
        } else {
            return false;
        }
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
