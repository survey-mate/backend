package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.member.Member;

@RequiredArgsConstructor
@Service
public class SurveyServiceFacade {
    // TODO
    // Point쪽 Service 구현 완료시 연결
    private final SurveyService surveyService;

    @Transactional
    public void answerSurvey(Member respondent, Long surveyId) {
        // TODO
        // Point 지급하는 로직 필요
        surveyService.addAnswer(respondent, surveyId);
    }
}
