/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mouka.ebook.entity.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 *
 * @author nasser
 */
@Documented
@Pattern.List({
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
            message = "invalid email")
})
@Target({ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD, 
    ElementType.PARAMETER,
    ElementType.TYPE})
@Constraint(validatedBy = com.mouka.ebook.entity.constraint.handler.EmailHandler.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default "invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
        
    @Documented
    @Target({ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD, 
        ElementType.PARAMETER,
        ElementType.TYPE}
    )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List{
        Email[] value();
    }
}
