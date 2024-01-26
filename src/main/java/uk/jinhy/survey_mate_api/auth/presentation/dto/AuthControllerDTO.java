package uk.jinhy.survey_mate_api.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

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
        private String mailTitle;

    }

    @Builder
    @Getter
    public static class LoginRequestDTO {
        private String id;

        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MailCodeRequestDTO {

        @NotNull
        private String code;

        @NotNull
        private String emailAddress;

    }

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


    @Builder
    @Getter
    public static class PasswordResetCodeRequestDTO {

        private String emailAddress;

        private String code;

    }

    @Builder
    @Getter
    public static class PasswordResetRequestDTO {

        private String passwordResetToken;

        private String newPassword;

    }

    @Getter
    @Builder
    public static class PasswordUpdateRequestDTO {

        private String currentPassword;

        private String newPassword;

    }

    @Getter
    @Builder
    public static class MemberResponseDTO {

        private Member member;

    }

    @Getter
    @Builder
    public static class JwtResponseDTO {

        private String jwt;

    }

    @Getter
    @Builder
    public static class EmailCodeResponseDTO {

        private String emailValidationToken;

    }

    @Getter
    @Builder
    public static class PasswordResetCodeResponseDTO {

        private String passwordRestValidationToken;

    }

}
