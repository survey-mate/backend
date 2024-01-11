package uk.jinhy.survey_mate_api.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SurveyStatus {

    PROGRESSING("PROGRESSING", "진행중"),
    CLOSE("CLOSE", "진행완료");

    private String key;
    private String description;
}
