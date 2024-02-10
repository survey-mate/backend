package uk.jinhy.survey_mate_api.survey.application.service;

import java.util.List;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO.CreateSurveyDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;

public interface SurveyService {

    void createSurvey(Member registrant, CreateSurveyDTO dto);

    void editSurvey(Member registrant, SurveyServiceDTO.EditSurveyDTO dto);

    void deleteSurvey(Member registrant, Long surveyId);

    void addAnswer(Member respondent, String rewardUrl);

    Survey getSurvey(Long surveyId);

    List<Survey> getSurveyList(int pageNumber);

    List<Survey> getMySurveyList(Member registrant);

    List<Survey> getAnsweredSurveyList(Member respondent);

    List<Survey> getRecentSurveyList();

    boolean isResponded(Survey survey, Member member);
}
