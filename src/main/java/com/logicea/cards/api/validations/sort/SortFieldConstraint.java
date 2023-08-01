package com.logicea.cards.api.validations.sort;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = SortFieldValidator.class)
public @interface SortFieldConstraint {

    String message() default "Invalid sort field";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
