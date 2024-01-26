package uk.jinhy.survey_mate_api.statement.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;

import java.util.List;

public class StatementControllerDTO {
    @Builder
    @Getter
    public static class GetStatementRequestDTO {
        private Member member;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class StatementListDTO {
        private List<Statement> statements;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TotalAmountDTO {
        private Long totalAmount;
    }
}
