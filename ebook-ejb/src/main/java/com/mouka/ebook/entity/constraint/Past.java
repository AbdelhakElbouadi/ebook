package com.mouka.ebook.entity.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Abdelhak
 */
@Documented
@Target({ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD, 
    ElementType.PARAMETER,
    ElementType.TYPE})
@Constraint(validatedBy = com.mouka.ebook.entity.constraint.handler.PastValidationHandler.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Past {
    String message() default "the provided date should be past.";
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
        Past[] value();
    }
}
