package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.common.util.Util;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Answer;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.domain.repository.SurveyRepository;

import java.time.LocalDateTime;
import java.util.List;

// TODO
// Exception 구현 시 예외 처리 추가
@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

     public Survey createSurvey(Member registrant, SurveyServiceDTO.CreateSurveyDTO dto) {
         Survey survey = Survey.builder()
                 .reward(dto.getReward())
                 .endedAt(LocalDateTime.now().plusDays(dto.getPeriod()))
                 .linkUrl(dto.getLinkUrl())
                 .rewardUrl(Util.generateRandomString())
                 .title(dto.getTitle())
                 .description(dto.getDescription())
                 .registrant(registrant)
                 .build();
         surveyRepository.save(survey);
         return survey;
     }

    @Transactional
    public void editSurvey(Member registrant, SurveyServiceDTO.EditSurveyDTO dto) {
        Long surveyId = dto.getSurveyId();
        Survey survey = surveyRepository.findBySurveyId(surveyId).get();

        if (!survey.getRegistrant().equals(registrant)) {
            return;
        }

        String newTitle = dto.getTitle();
        if (newTitle != null) {
            survey.updateTitle(newTitle);
        }

        String newDescription = dto.getDescription();
        if (newDescription != null) {
            survey.updateDescription(newDescription);
        }

        String newLinkUrl = dto.getLinkUrl();
        if (newLinkUrl != null) {
            survey.updateLinkUrl(newLinkUrl);
        }
    }

    @Transactional
    public void deleteSurvey(Member registrant, Long surveyId) {
        Survey survey = surveyRepository.findBySurveyId(surveyId).get();
        if (survey.getRegistrant().equals(registrant)) {
            surveyRepository.deleteById(surveyId);
        }
    }

    public void addAnswer(Member respondent, Long surveyId) {
        Survey survey = surveyRepository.findBySurveyId(surveyId).get();
        if (!survey.isAnswered(respondent)) {
            return;
        }
        Answer answer = Answer.builder()
                .survey(survey)
                .respondent(respondent)
                .build();
        survey.addAnswer(answer);
        surveyRepository.save(survey);
    }

    public Survey getSurvey(Long surveyId) {
        return surveyRepository.findBySurveyId(surveyId).get();
    }

    public List<Survey> getSurveyList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return surveyRepository.findByEndedAtIsBefore(pageable, LocalDateTime.now());
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
}
