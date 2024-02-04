package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;

@RequiredArgsConstructor
@Service
public class SurveyServiceFacade {

    private final SurveyService surveyService;
    private final StatementService statementService;

    @Transactional
    public void createSurvey(Member registrant, SurveyServiceDTO.CreateSurveyDTO dto) {
        StatementServiceDTO.PayPointDTO payPointDTO = StatementServiceDTO
            .PayPointDTO
            .builder()
            .description("설문조사 추가")
            .amount(SurveyRegistrationFee.getFee(dto.getPeriod()))
            .build();
        surveyService.createSurvey(registrant, dto);
        statementService.payPoint(registrant, payPointDTO);
    }

    @Transactional
    public void answerSurvey(Member respondent, String rewardUrl) {
        StatementServiceDTO.EarnPointDTO earnPointDTO = StatementServiceDTO
            .EarnPointDTO
            .builder()
            .description("설문조사 응답")
            .amount(1L)
            .build();
        surveyService.addAnswer(respondent, rewardUrl);
        statementService.earnPoint(respondent, earnPointDTO);
    }
}
