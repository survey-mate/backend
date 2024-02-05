package uk.jinhy.survey_mate_api.statement.application.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

public class StatementServiceDTO {

    @Builder
    @Getter
    public static class PayPointDTO {

        @Min(1)
        private Long amount;
        private String description;
    }

    @Builder
    @Getter
    public static class EarnPointDTO {

        @Min(1)
        private Long amount;
        private String description;
    }
}
