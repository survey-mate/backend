package uk.jinhy.survey_mate_api.statement.application.dto;

import lombok.Builder;
import lombok.Getter;

public class StatementServiceDTO {

    @Builder
    @Getter
    public static class CreateStatementDTO {

        private Long amount;
        private String description;
    }
}
