package uk.jinhy.survey_mate_api.survey.application.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.domain.repository.SurveyRepository;

@RequiredArgsConstructor
@Service
public class SurveyQueryServiceImpl implements SurveyQueryService {

    private final SurveyRepository surveyRepository;

    public Survey getSurvey(Long surveyId) {
        return surveyRepository.findBySurveyId(surveyId)
            .orElseThrow(() -> new GeneralException(Status.SURVEY_NOT_FOUND));
    }

    public List<Survey> getSurveyList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return surveyRepository.findByEndedAtAfterOrderByEndedAt(
            LocalDateTime.now(),
            pageable
        );
    }

    public List<Survey> getMySurveyList(Member registrant) {
        return surveyRepository.findByRegistrant(registrant);
    }

    public List<Survey> getAnsweredSurveyList(Member respondent) {
        return surveyRepository.findByRespondent(respondent);
    }

    public List<Survey> getRecentSurveyList() {
        return surveyRepository.findRecentSurvey();
    }

    public boolean isResponded(Survey survey, Member member) {
        return survey.isAnswered(member);
    }
}
