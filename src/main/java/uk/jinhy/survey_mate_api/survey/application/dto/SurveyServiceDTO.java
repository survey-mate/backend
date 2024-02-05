package uk.jinhy.survey_mate_api.survey.application.dto;

import lombok.Builder;
import lombok.Getter;

public class SurveyServiceDTO {

    @Builder
    @Getter
    public static class CreateSurveyDTO {

        private String linkUrl;
        private String title;
        private String description;
        private Long reward;
        private Long period;
    }

    @Builder
    @Getter
    public static class EditSurveyDTO {

        private Long surveyId;
        private String linkUrl;
        private String title;
        private String description;
    }
}
