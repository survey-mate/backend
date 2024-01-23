package uk.jinhy.survey_mate_api.statement.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;

public class StatementControllerDTO {
    @Builder
    @Getter
    public static class GetStatementDTO {
        private Member member;
    }
}
