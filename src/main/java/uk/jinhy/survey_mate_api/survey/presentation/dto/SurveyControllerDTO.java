package uk.jinhy.survey_mate_api.survey.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;
import uk.jinhy.survey_mate_api.survey.domain.entity.SurveyFormLink;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyControllerDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateSurveyRequestDTO {
        @NotBlank
        public String title;
        @NotBlank
        public String description;
        @SurveyFormLink
        public String linkUrl;
        @Min(1)
        @Max(7)
        public Long period;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EditSurveyRequestDTO {
        @NotBlank
        public String title;
        @NotBlank
        public String description;
        @SurveyFormLink
        public String linkUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SurveyDTO {
        public Long surveyId;
        public String title;
        public String description;
        public LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SurveyListDTO {
        public List<SurveyDTO> surveys;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SurveyDetailDTO {
        public Long surveyId;
        public String title;
        public String description;
        public LocalDateTime createdAt;
        public String registrantName;
        public String linkUrl;
        public boolean isResponded;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RewardResultDTO {
        public Long reward;
    }
}
