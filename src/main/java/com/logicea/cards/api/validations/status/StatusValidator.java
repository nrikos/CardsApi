package com.logicea.cards.api.validations.status;

import com.logicea.cards.api.payloads.CardStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StatusValidator implements ConstraintValidator<StatusConstraint, CardStatus> {
    @Override
    public boolean isValid(CardStatus value, ConstraintValidatorContext context) {
        return value == null || Arrays.stream(CardStatus.values()).map(Enum::name).anyMatch(status -> status.equalsIgnoreCase(value.name()));
    }
}
