package uk.jinhy.survey_mate_api.common.email.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MailServiceDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SendEmailDTO {

        @NotNull
        @Schema(description = "받는 사람 이메일 주소", example = "mingmingmon@kyonggi.ac.kr")
        private String receiver;

        @NotNull
        @Schema(description = "이메일 템플릿 파일 이름")
        private String templateFileName;

        @NotNull
        @Schema(description = "이메일 템플릿 Context")
        private Map<String, Object> templateContext;

        @Schema(description = "이메일 제목", example = "[썰매 (Survey Mate)] 회원가입을 위한 인증 코드입니다.")
        private String subject;

        public void setTemplateContext(Map<String, Object> templateContext) {
            this.templateContext = templateContext;
        }

    }

}
