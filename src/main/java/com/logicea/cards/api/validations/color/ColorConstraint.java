package com.logicea.cards.api.validations.color;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ColorValidator.class)
public @interface ColorConstraint {

    String message() default "Invalid color";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
