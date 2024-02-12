package uk.jinhy.survey_mate_api.survey.application.service;

import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyCommandServiceDTO;

public interface SurveyServiceFacade {

    void createSurvey(Member registrant, SurveyCommandServiceDTO.CreateSurveyDTO dto);

    void answerSurvey(Member respondent, String rewardUrl);
}
