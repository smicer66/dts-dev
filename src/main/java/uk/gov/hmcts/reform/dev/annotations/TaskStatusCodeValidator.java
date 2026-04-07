package uk.gov.hmcts.reform.dev.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaskStatusCodeValidator implements ConstraintValidator<ValidTaskStatusCode, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidTaskStatusCode constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    //Check to see if the value being validated equals any of the enum values of TaskStatus
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null)
        {
            return true;
        }

        for(Enum<?> e : enumClass.getEnumConstants())
        {
            try
            {

                if(e.name().equals(value)){
                    return true;
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
