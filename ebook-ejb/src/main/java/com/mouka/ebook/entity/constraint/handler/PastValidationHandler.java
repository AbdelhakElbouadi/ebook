package com.mouka.ebook.entity.constraint.handler;

import com.mouka.ebook.entity.constraint.Past;
import java.util.Calendar;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Abdelhak
 */
public class PastValidationHandler  implements ConstraintValidator<Past,Date>{

    
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        return today.after(value);
    }
    
}
