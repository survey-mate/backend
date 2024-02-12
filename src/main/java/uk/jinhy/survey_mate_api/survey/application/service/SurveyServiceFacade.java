package uk.jinhy.survey_mate_api.survey.application.service;

import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyCommandServiceDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;

public interface SurveyServiceFacade {

    Survey createSurvey(Member registrant, SurveyCommandServiceDTO.CreateSurveyDTO dto);

    void answerSurvey(Member respondent, String rewardUrl);
}
