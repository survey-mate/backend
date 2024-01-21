package uk.jinhy.survey_mate_api.survey.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class SurveyControllerDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateSurveyRequestDTO {
        public String title;
        public String description;
        public String linkUrl;
        public Long period;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EditSurveyRequestDTO {
        public String title;
        public String description;
        public String linkUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SurveyDTO {
        public Long surveyId;
        public String title;
        public String description;
        public LocalDate createdAt;
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
        public LocalDate createdAt;
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
