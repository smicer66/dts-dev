package uk.gov.hmcts.reform.dev.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TaskStatusCodeValidator.class)
@Documented
public @interface ValidTaskStatusCode {
    Class<? extends Enum<?>> enumClass();

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    String message() default "Task status code does not match possible values expected.";
}
