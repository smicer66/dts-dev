package uk.gov.hmcts.reform.dev.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusCodeValidator implements ConstraintValidator<ValidStatusCode, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidStatusCode constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null)
        {
            return true;
        }

        for(Enum<?> e : enumClass.getEnumConstants())
        {
            try
            {
                String statusCode = (String)enumClass.getClass().getMethod("getStatusCode").invoke(e);

                if(statusCode.equals(value)){
                    return true;
                }
            }
            catch(Exception exception)
            {
                return false;
            }
        }

        return false;
    }
}
