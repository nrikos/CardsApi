package com.logicea.cards.api.validations.sort;

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
@Constraint(validatedBy = SortOrderValidator.class)
public @interface SortOrderConstrain {

    String message() default "Invalid sort field";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
