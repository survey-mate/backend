package uk.jinhy.survey_mate_api.survey.domain.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

import java.util.regex.Pattern;

public class SurveyFormLinkValidator implements ConstraintValidator<SurveyFormLink, String> {
    private static final String GOOGLE_FORM_PATTERN = "https:\\/\\/(docs\\.google\\.com\\/forms|forms\\.gle)\\/[^\\s]+";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            throw new GeneralException(Status.BAD_SURVEY_FORM_LINK);
        }
        return Pattern.matches(GOOGLE_FORM_PATTERN, value);
    }
}