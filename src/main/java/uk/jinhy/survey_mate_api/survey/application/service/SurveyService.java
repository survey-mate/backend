package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.common.util.Util;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Answer;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.domain.repository.SurveyRepository;

import java.time.LocalDateTime;
import java.util.List;

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
        Survey survey = surveyRepository.findBySurveyId(surveyId).orElseThrow(
                () -> new GeneralException(Status.SURVEY_NOT_FOUND)
        );

        if (!survey.getRegistrant().equals(registrant)) {
            throw new GeneralException(Status.WRONG_REGISTRANT);
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
        Survey survey = surveyRepository.findBySurveyId(surveyId).orElseThrow(
                () -> new GeneralException(Status.SURVEY_NOT_FOUND)
        );
        if (survey.getRegistrant().equals(registrant)) {
            surveyRepository.deleteById(surveyId);
        }
    }

    public void addAnswer(Member respondent, String rewardUrl) {
        Survey survey = surveyRepository.findByRewardUrl(rewardUrl).orElseThrow(
                () -> new GeneralException(Status.SURVEY_NOT_FOUND)
        );
        if (!survey.isAnswered(respondent)) {
            throw new GeneralException(Status.ALREADY_ANSWERED);
        }
        Answer answer = Answer.builder()
                .survey(survey)
                .respondent(respondent)
                .build();
        survey.addAnswer(answer);
        surveyRepository.save(survey);
    }

    public Survey getSurvey(Long surveyId) {
        return surveyRepository.findBySurveyId(surveyId).orElseThrow(() -> new GeneralException(Status.SURVEY_NOT_FOUND));
    }

    public List<Survey> getSurveyList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return surveyRepository.findByEndedAtIsBeforeOrderByCreatedAtDesc(pageable, LocalDateTime.now());
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
