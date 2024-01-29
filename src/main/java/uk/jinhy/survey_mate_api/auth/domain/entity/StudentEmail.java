package uk.jinhy.survey_mate_api.auth.domain.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = StudentEmailValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentEmail {

    String message() default "올바른 학생용 이메일 계정 주소가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
