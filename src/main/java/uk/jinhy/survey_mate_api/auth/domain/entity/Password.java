package uk.jinhy.survey_mate_api.auth.domain.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "비밀번호는 대소문자, 숫자, 특수문자(@$!*#?&) 포함 8자~15자 이내여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
