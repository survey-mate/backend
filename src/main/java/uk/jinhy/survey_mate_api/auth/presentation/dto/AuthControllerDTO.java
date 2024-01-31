package uk.jinhy.survey_mate_api.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Password;

public class AuthControllerDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CertificateCodeRequestDTO {

        @NotNull
        @Schema(description = "받는 사람 이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String receiver;

        @Schema(description = "이메일 제목", example = "[썰매 (Survey Mate)] 회원가입을 위한 인증 코드입니다.")
        private String mailSubject;

        @Schema(description = "템플릿 속 제목", example = "인증 코드")
        private String mailTitle;

    }

    @Builder
    @Getter
    public static class LoginRequestDTO {

        @Schema(description = "아이디(학교 계정 이메일 주소)", example = "mingmingmon@kyonggi.ac.kr")
        private String id;

        @Schema(description = "비밀번호", example = "1234asdf!")
        private String password;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MailCodeRequestDTO {

        @NotNull
        @Schema(description = "인증코드", example = "124578")
        private String code;

        @NotNull
        @Schema(description = "학교 계정 이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String emailAddress;

    }

    @Builder
    @Getter
    public static class MemberRequestDTO {

        @NotNull
        @Schema(description = "학교 계정 이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String memberId;

        @NotNull
        @Schema(description = "닉네임", example = "로그")
        private String nickname;

        @NotNull
        @Password
        @Schema(description = "비밀번호", example = "1234asdf!")
        private String password;

        @NotNull
        @Schema(description = "이메일 인증 토큰", example = "aosdjgr")
        private String emailToken;

        @NotNull
        @Schema(description = "서비스 이용약관 동의여부", example = "true")
        private boolean serviceConsent;

        @NotNull
        @Schema(description = "개인정보 수집 및 이용 동의여부", example = "true")
        private boolean privacyConsent;

    }

    @Builder
    @Getter
    public static class PasswordResetCodeRequestDTO {

        @NotNull
        @Schema(description = "학교 계정 이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String emailAddress;

        @NotNull
        @Schema(description = "인증코드", example = "124578")
        private String code;

    }

    @Builder
    @Getter
    public static class PasswordResetRequestDTO {

        @NotNull
        @Schema(description = "이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String emailAddress;

        @NotNull
        @Schema(description = "계정 인증 토큰", example = "aosdjgr")
        private String passwordResetToken;

        @NotNull
        @Password
        @Schema(description = "새로운 비밀번호", example = "asdf1234$")
        private String newPassword;

    }

    @Getter
    @Builder
    public static class PasswordUpdateRequestDTO {

        @NotNull
        @Schema(description = "현재 비밀번호", example = "1234asdf!")
        private String currentPassword;

        @NotNull
        @Password
        @Schema(description = "새로운 비밀번호", example = "asdf1234$")
        private String newPassword;

    }

    @NoArgsConstructor
    @Getter
    public static class DeleteAccountRequestDTO {

        @NotNull
        @Schema(description = "현재 비밀번호", example = "1234asdf!")
        private String currentPassword;

    }

    @Getter
    @Builder
    public static class MemberResponseDTO {

        private String id;

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

    @Getter
    @Builder
    public static class IsStudentAccountResponseDTO {

        private boolean isStudentAccount;

    }

}
