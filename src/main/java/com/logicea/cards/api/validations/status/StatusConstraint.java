package com.logicea.cards.api.validations.status;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target( ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StatusValidator.class})
public @interface StatusConstraint {

        String message() default "Invalid card status";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
