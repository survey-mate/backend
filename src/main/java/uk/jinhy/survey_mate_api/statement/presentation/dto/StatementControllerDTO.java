package uk.jinhy.survey_mate_api.statement.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;

public class StatementControllerDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class StatementDTO {

        private LocalDateTime createdAt;
        private Long amount;
        private Long balance;
        private String description;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class StatementListDTO {

        private List<StatementDTO> statements;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TotalAmountDTO {

        private Long totalAmount;
    }
}
