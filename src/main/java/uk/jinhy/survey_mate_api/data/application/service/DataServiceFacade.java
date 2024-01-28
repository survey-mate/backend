package uk.jinhy.survey_mate_api.data.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.survey.application.service.SurveyRegistrationFee;


@RequiredArgsConstructor
@Service
public class DataServiceFacade {
    private final DataService dataService;
    private final StatementService statementService;

    @Transactional
    public void buyData(Member buyer, Long dataId) {
        Long price = dataService.buyData(buyer, dataId);

        StatementServiceDTO.CreateStatementDTO createStatementDTO = StatementServiceDTO
                .CreateStatementDTO
                .builder()
                .description("설문조사 구매")
                .amount(price * -1)
                .build();
        statementService.createStatement(buyer, createStatementDTO);
    }
}
