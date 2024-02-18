package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.statement.application.dto.StatementServiceDTO;
import uk.jinhy.survey_mate_api.statement.application.service.StatementService;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyCommandServiceDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;

@RequiredArgsConstructor
@Service
public class SurveyServiceFacadeImpl implements SurveyServiceFacade {

    private final SurveyCommandService surveyService;
    private final StatementService statementService;

    @Transactional
    public Survey createSurvey(Member registrant, SurveyCommandServiceDTO.CreateSurveyDTO dto) {
        if (!registrant.isStudent()) {
            throw new GeneralException(Status.NON_STUDENT_REGISTRANT);
        }
        StatementServiceDTO.PayPointDTO payPointDTO = StatementServiceDTO
            .PayPointDTO
            .builder()
            .description("설문조사 추가")
            .amount(SurveyRegistrationFee.getFee(dto.getPeriod()))
            .build();
        Survey survey = surveyService.createSurvey(registrant, dto);
        statementService.payPoint(registrant, payPointDTO);
        return survey;
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
