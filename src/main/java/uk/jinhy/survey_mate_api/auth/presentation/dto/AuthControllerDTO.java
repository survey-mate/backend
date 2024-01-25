package uk.jinhy.survey_mate_api.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthControllerDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CertificateCodeRequestDTO {

        @NotNull
        private String receiver;

        @Schema(description = "이메일 제목", example = "!썰매! 회원가입 전 학교 이메일을 인증해주세요. 이메일 인증 코드 전송")
        private String mailSubject;

        @Schema(description = "템플릿 속 제목", example = "학교 이메일 확인용 인증코드")
        private String title;

    }

    @Builder
    @Getter
    public static class LoginControllerDTO {
        private String id;

        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MailCodeControllerDTO {

        @NotNull
        private String code;

        @NotNull
        private String emailAddr;

    }

    public static class MemberControllerDTO {
        @Builder
        @Getter
        public static class MemberRequestDTO{

            private String memberId;

            private String nickname;

            private String password;

            private String emailToken;

            private boolean messageConsent;

            private boolean marketingConsent;
        }

    }

    @Builder
    @Getter
    public static class PasswordResetCodeDTO {

        private String emailAddr;

        private String code;

    }

    @Builder
    @Getter
    public static class PasswordResetControllerDTO {

        private String passwordResetToken;

        private String newPassword;

    }

    @Getter
    @Builder
    public static class PasswordUpdateRequestDTO {

        private String currentPassword;

        private String newPassword;

    }

}
