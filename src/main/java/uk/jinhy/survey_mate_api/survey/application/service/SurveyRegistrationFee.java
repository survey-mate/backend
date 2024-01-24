package uk.jinhy.survey_mate_api.survey.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SurveyRegistrationFee {
    ONE_DAY(1L, 10L),
    TWO_DAYS(2L, 15L),
    THREE_DAYS(3L, 20L),
    FOUR_DAYS(4L, 25L),
    FIVE_DAYS(5L, 30L),
    SIX_DAYS(6L, 35L),
    SEVEN_DAYS(7L, 40L),
    ;
    private final Long periodOfSurvey;
    private final Long fee;

    public static Long getFee(Long period) {
        return Arrays.stream(SurveyRegistrationFee.values())
                .filter(value -> value.periodOfSurvey.equals(period))
                .findAny()
                .orElseThrow(() -> new GeneralException(Status.WRONG_PERIOD_VALUE))
                .getFee();
    }
}
