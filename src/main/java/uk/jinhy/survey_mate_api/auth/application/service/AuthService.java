package uk.jinhy.survey_mate_api.auth.application.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import uk.jinhy.survey_mate_api.common.email.service.dto.MailServiceDTO;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.common.util.Util;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final MailCodeRepository mailCodeRepository;

    private final EmailTokenRepository emailTokenRepository;

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final StatementService statementService;

    private final AuthProvider authProvider;

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

        StatementServiceDTO.EarnPointDTO earnPointDTO = StatementServiceDTO
            .EarnPointDTO
            .builder()
            .description("회원가입 축하 포인트")
            .amount(20L)
            .build();

        statementService.earnPoint(member, earnPointDTO);

        return AuthControllerDTO.MemberResponseDTO.builder()
            .id(member.getMemberId())
            .build();
    }

    public AuthControllerDTO.JwtResponseDTO login(AuthServiceDTO.LoginDTO dto) {
        String id = dto.getId();
        String password = dto.getPassword();

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GeneralException(Status.PASSWORD_INCORRECT);
        }

        return AuthControllerDTO.JwtResponseDTO.builder()
            .jwt(authProvider.generateToken(id, password))
            .build();
    }

    public void sendMailCode(AuthServiceDTO.CertificateCodeDTO dto) {
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

        Map<String, Object> templateContext = new HashMap<>();
        templateContext.put("title", "인증코드");
        templateContext.put("code", mailValidationCode);

        MailServiceDTO.SendEmailDTO sendEmailDTO = MailServiceDTO.SendEmailDTO.builder()
            .receiver(dto.getReceiver())
            .templateFileName("SurveyEmail.html")
            .subject("[썰매 (Survey Mate)] 회원가입을 위한 인증 코드입니다.")
            .templateContext(templateContext)
            .build();

        mailService.sendEmail(sendEmailDTO);
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

    public void sendPasswordResetCode(AuthServiceDTO.CertificateCodeDTO dto) {
        String memberId = dto.getReceiver();
        Member member = getMemberById(memberId);

        String accountValidationCode = Util.generateRandomNumberString(6);
        PasswordResetCode passwordResetCode = PasswordResetCode.builder()
            .code(accountValidationCode)
            .emailAddress(memberId)
            .createdAt(LocalDateTime.now())
            .build();

        passwordResetCodeRepository.save(passwordResetCode);

        Map<String, Object> templateContext = new HashMap<>();
        templateContext.put("title", "인증코드");
        templateContext.put("code", accountValidationCode);

        MailServiceDTO.SendEmailDTO sendEmailDTO = MailServiceDTO.SendEmailDTO.builder()
            .receiver(dto.getReceiver())
            .templateFileName("SurveyEmail.html")
            .subject("[썰매 (Survey Mate)] 계정 비밀번호 재설정을 위한 인증 코드입니다.")
            .templateContext(templateContext)
            .build();

        mailService.sendEmail(sendEmailDTO);
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

    public void updateNickname(AuthServiceDTO.NicknameUpdateDTO dto) {
        Member member = getCurrentMember();
        String newNickname = dto.getNewNickname();

        if (checkNickname(newNickname)) {
            throw new GeneralException(Status.NICKNAME_ALREADY_EXIST);
        }

        member.changeNickname(newNickname);
        memberRepository.save(member);
    }

    public void deleteAccount(AuthServiceDTO.DeleteAccountDTO dto) {
        Member member = getCurrentMember();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new GeneralException(Status.PASSWORD_INCORRECT);
        }

        memberRepository.delete(member);
    }

    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public boolean isStudentAccount() {
        return getCurrentMember().isStudent();
    }

    public Member getCurrentMember() {
        String memberId = authProvider.getUsernameFromAuthentication();
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

}
