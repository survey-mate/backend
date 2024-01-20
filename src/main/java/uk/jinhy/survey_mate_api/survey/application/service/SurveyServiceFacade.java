package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.member.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;

@RequiredArgsConstructor
@Service
public class SurveyServiceFacade {
    private final SurveyService surveyService;

    @Transactional
    public void createSurvey(Member registrant, SurveyServiceDTO.CreateSurveyDTO dto) {
        // TODO
        // Point 지불하는 로직 필요
        surveyService.createSurvey(registrant, dto);
    }

    @Transactional
    public void answerSurvey(Member respondent, Long surveyId) {
        // TODO
        // Point 지급하는 로직 필요
        surveyService.addAnswer(respondent, surveyId);
    }
}
