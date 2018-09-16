package com.mouka.ebook.entity.constraint.handler;

import com.mouka.ebook.entity.constraint.Email;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Abdelhak
 */
public class EmailHandler implements ConstraintValidator<Email, String>{

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        
        return Pattern.matches(pattern, value);
    }
    
}
