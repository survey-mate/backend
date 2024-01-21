package uk.jinhy.survey_mate_api.survey.presentation.converter;

import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.survey.application.dto.SurveyServiceDTO;
import uk.jinhy.survey_mate_api.survey.domain.entity.Survey;
import uk.jinhy.survey_mate_api.survey.presentation.dto.SurveyControllerDTO;

@Component
public class SurveyConverter {

    public SurveyServiceDTO.CreateSurveyDTO toServiceCreateSurveyDto(
            SurveyControllerDTO.CreateSurveyRequestDTO dto
    ) {
        return SurveyServiceDTO.CreateSurveyDTO.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .linkUrl(dto.getLinkUrl())
                .reward(1L)
                .period(dto.getPeriod())
                .build();
    }

    public SurveyServiceDTO.EditSurveyDTO toServiceEditSurveyDto(
            Long surveyId,
            SurveyControllerDTO.EditSurveyRequestDTO dto
    ) {
        return SurveyServiceDTO.EditSurveyDTO.builder()
                .surveyId(surveyId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .linkUrl(dto.getLinkUrl())
                .build();
    }

    public SurveyControllerDTO.SurveyDTO toSurveyDTO(Survey survey) {
        return SurveyControllerDTO.SurveyDTO.builder()
                .surveyId(survey.getSurveyId())
                .description(survey.getDescription())
                .title(survey.getTitle())
                .createdAt(survey.getCreatedAt())
                .build();
    }

    public SurveyControllerDTO.SurveyDetailDTO toSurveyDetailDto(boolean isResponded, Survey survey) {
        return SurveyControllerDTO.SurveyDetailDTO.builder()
                .surveyId(survey.getSurveyId())
                .createdAt(survey.getCreatedAt())
                .registrantName(survey.getRegistrant().getNickname())
                .isResponded(isResponded)
                .linkUrl(survey.getLinkUrl())
                .description(survey.getDescription())
                .title(survey.getTitle())
                .build();
    }
}
