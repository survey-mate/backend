package uk.jinhy.survey_mate_api.survey.application.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.survey_mate_api.auth.domain.entity.Member;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;
import uk.jinhy.survey_mate_api.common.util.Util;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO.CreateSurveyDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Answer;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.domain.repository.SurveyRepository;

@RequiredArgsConstructor
@Service
public class SurveyCommandServiceImpl implements SurveyCommandService {

    private final SurveyRepository surveyRepository;

    public void createSurvey(Member registrant, CreateSurveyDTO dto) {
        Survey survey = Survey.builder()
            .reward(dto.getReward())
            .endedAt(LocalDateTime.now().plusDays(dto.getPeriod()))
            .linkUrl(dto.getLinkUrl())
            .rewardUrl(Util.generateRandomString(10))
            .title(dto.getTitle())
            .description(dto.getDescription())
            .registrant(registrant)
            .build();
        surveyRepository.save(survey);
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

        if (!survey.getRegistrant().equals(registrant)) {
            throw new GeneralException(Status.WRONG_REGISTRANT);
        }

        surveyRepository.deleteById(surveyId);
    }

    public void addAnswer(Member respondent, String rewardUrl) {
        Survey survey = surveyRepository.findByRewardUrl(rewardUrl).orElseThrow(
            () -> new GeneralException(Status.SURVEY_NOT_FOUND)
        );
        if (survey.isAnswered(respondent)) {
            throw new GeneralException(Status.ALREADY_ANSWERED);
        }
        Answer answer = Answer.builder()
            .survey(survey)
            .respondent(respondent)
            .build();
        survey.addAnswer(answer);
        surveyRepository.save(survey);
    }
}
