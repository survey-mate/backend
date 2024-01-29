package uk.jinhy.survey_mate_api.auth.domain.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@Component
@RequiredArgsConstructor
public class StudentEmailValidator implements ConstraintValidator<StudentEmail, String> {

    @Override
    public void initialize(StudentEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String values, ConstraintValidatorContext context) {
        if(values == null){
            throw new GeneralException(Status.BAD_MAIL_ADDRESS);
        }

       return values.matches(".*\\.(ac\\.kr|edu)$");
    }

}
