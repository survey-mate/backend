package uk.jinhy.survey_mate_api.survey.application.service;

import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO.CreateSurveyDTO;

public interface SurveyCommandService {

    void createSurvey(Member registrant, CreateSurveyDTO dto);

    void editSurvey(Member registrant, SurveyServiceDTO.EditSurveyDTO dto);

    void deleteSurvey(Member registrant, Long surveyId);

    void addAnswer(Member respondent, String rewardUrl);
}
