package uk.jinhy.survey_mate_api.statement.presentation.converter;

import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.statement.domain.entity.Statement;
import uk.jinhy.survey_mate_api.statement.presentation.StatementController;
import uk.jinhy.survey_mate_api.statement.presentation.dto.StatementControllerDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatementConverter {
    public StatementControllerDTO.StatementDTO toControllerStatementDto(Statement statement) {
        return StatementControllerDTO.StatementDTO.builder()
                .createdAt(statement.getCreatedAt())
                .amount(statement.getAmount())
                .balance(statement.getBalance())
                .description(statement.getDescription())
                .build();
    }

    public StatementControllerDTO.StatementListDTO toControllerStatementListDto(List<Statement> statements) {
        return StatementControllerDTO.StatementListDTO.builder()
                .statements(statements.stream().map(statement -> toControllerStatementDto(statement)).collect(Collectors.toList()))
                .build();
    }
}
