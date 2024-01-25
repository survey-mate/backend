package uk.jinhy.survey_mate_api.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateCodeRequestDTO {

    @NotNull
    private String receiver;

    @Schema(description = "이메일 제목", example = "!썰매! 회원가입 전 학교 이메일을 인증해주세요. 이메일 인증 코드 전송")
    private String mailSubject;

    @Schema(description = "템플릿 속 제목", example = "학교 이메일 확인용 인증코드")
    private String title;

}
