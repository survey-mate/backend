package uk.jinhy.survey_mate_api.auth.presentation.converter;

import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.auth.application.dto.AuthServiceDTO;
import uk.jinhy.survey_mate_api.auth.presentation.dto.AuthControllerDTO;

@Component
public class AuthConverter {

    public AuthServiceDTO.CertificateCodeDTO toServiceCertificateCodeDTO(
        AuthControllerDTO.CertificateCodeRequestDTO dto
    ) {
        return AuthServiceDTO.CertificateCodeDTO.builder()
            .receiver(dto.getReceiver())
            .build();
    }

    public AuthServiceDTO.LoginDTO toServiceLoginDTO(
        AuthControllerDTO.LoginRequestDTO dto
    ) {
        return AuthServiceDTO.LoginDTO.builder()
            .id(dto.getId())
            .password(dto.getPassword())
            .build();
    }

    public AuthServiceDTO.MailCodeDTO toServiceMailCodeDTO(
        AuthControllerDTO.MailCodeRequestDTO dto
    ) {
        return AuthServiceDTO.MailCodeDTO.builder()
            .code(dto.getCode())
            .emailAddress(dto.getEmailAddress())
            .build();
    }

    public AuthServiceDTO.MemberDTO toServiceMemberDTO(
        AuthControllerDTO.MemberRequestDTO dto
    ) {
        return AuthServiceDTO.MemberDTO.builder()
            .memberId(dto.getMemberId())
            .nickname(dto.getNickname())
            .password(dto.getPassword())
            .emailToken(dto.getEmailToken())
            .serviceConsent(dto.isServiceConsent())
            .privacyConsent(dto.isPrivacyConsent())
            .build();
    }

    public AuthServiceDTO.PasswordResetCodeDTO toServicePasswordResetCodeDTO(
        AuthControllerDTO.PasswordResetCodeRequestDTO dto
    ) {
        return AuthServiceDTO.PasswordResetCodeDTO.builder()
            .emailAddress(dto.getEmailAddress())
            .code(dto.getCode())
            .build();
    }

    public AuthServiceDTO.PasswordResetDTO toServicePasswordResetDTO(
        AuthControllerDTO.PasswordResetRequestDTO dto
    ) {
        return AuthServiceDTO.PasswordResetDTO.builder()
            .emailAddress(dto.getEmailAddress())
            .passwordResetToken(dto.getPasswordResetToken())
            .newPassword(dto.getNewPassword())
            .build();
    }

    public AuthServiceDTO.PasswordUpdateDTO toServicePasswordUpdateDTO(
        AuthControllerDTO.PasswordUpdateRequestDTO dto
    ) {
        return AuthServiceDTO.PasswordUpdateDTO.builder()
            .currentPassword(dto.getCurrentPassword())
            .newPassword(dto.getNewPassword())
            .build();
    }

    public AuthServiceDTO.DeleteAccountDTO toServiceDeleteAccountDTO(
        AuthControllerDTO.DeleteAccountRequestDTO dto
    ) {
        return AuthServiceDTO.DeleteAccountDTO.builder()
            .currentPassword(dto.getCurrentPassword())
            .build();
    }

    public AuthServiceDTO.NicknameUpdateDTO toServiceNicknameUpdateDTO(
            AuthControllerDTO.NicknameUpdateRequestDTO dto
    ) {
        return AuthServiceDTO.NicknameUpdateDTO.builder()
                .newNickname(dto.getNewNickname())
                .build();
    }

}
