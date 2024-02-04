package uk.jinhy.survey_mate_api.common.email.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MailSenderDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class SendMailDTO {

        public String from;
        public String to;
        public String content;
        public String subject;
    }
}
