package uk.jinhy.survey_mate_api.statement.presentation.converter;

import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.presentation.dto.StatementControllerDTO;

public class StatementConverter {
    public static StatementServiceDTO.CreateStatementDTO convertCreateDto(StatementControllerDTO.CreateStatementDTO controllerDTO) {
        return StatementServiceDTO.CreateStatementDTO.builder()
                .amount(controllerDTO.getAmount())
                .description(controllerDTO.getDescription())
                .build();
    }
}
