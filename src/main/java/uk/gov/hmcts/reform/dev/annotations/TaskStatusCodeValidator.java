package uk.gov.hmcts.reform.dev.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

import java.lang.reflect.Method;

public class TaskStatusCodeValidator implements ConstraintValidator<ValidTaskStatusCode, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidTaskStatusCode constraintAnnotation) {
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
//                System.out.println("E Constants..." + e.name());
//                Class C = TaskStatus.class;//enumClass.getClass()
//                System.out.println("C..." + C.getClass().getName());
//                Method m = TaskStatus.class.getMethod("getCode");
//
//                System.out.println("m..." + m.getName());
//                m.invoke(e);
//                String statusCode = (String)enumClass.getClass().getMethod("getCode").invoke(e);
//
//                System.out.println("statusCode..." + statusCode);
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
