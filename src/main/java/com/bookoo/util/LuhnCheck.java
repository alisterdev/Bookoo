package com.bookoo.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * This is a class that defines an annotation for a Bean Validation You can
 * create your own bean validations by using this as your template
 * 
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LuhnCheckValidator.class)
public @interface LuhnCheck {
	String message() default "{com.bookoo.LuhnCheck.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
