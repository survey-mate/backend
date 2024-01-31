package uk.jinhy.survey_mate_api.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Password;

public class AuthServiceDTO {

    @Builder
    @Getter
    @Setter
    public static class CertificateCodeDTO {

        private String receiver;

        private String mailSubject;

        private String mailTitle;

    }

    @Builder
    @Getter
    public static class LoginDTO {

        private String id;

        private String password;

    }

    @Builder
    @Getter
    public static class MailCodeDTO {

        private String code;

        private String emailAddress;

    }

    @Builder
    @Getter
    public static class MemberDTO {

        private String memberId;

        private String nickname;

        private String password;

        private String emailToken;

        private boolean serviceConsent;

        private boolean privacyConsent;

    }

    @Builder
    @Getter
    public static class PasswordResetCodeDTO {

        private String emailAddress;

        private String code;

    }

    @Builder
    @Getter
    public static class PasswordResetDTO {

        private String passwordResetToken;

        private String newPassword;

    }

    @Getter
    @Builder
    public static class PasswordUpdateDTO {

        private String currentPassword;

        private String newPassword;

    }

    @Getter
    @Builder
    public static class DeleteAccountDTO {

        private String currentPassword;

    }

}
