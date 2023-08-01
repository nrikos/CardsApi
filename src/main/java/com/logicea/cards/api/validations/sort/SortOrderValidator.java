package com.logicea.cards.api.validations.sort;

import com.logicea.cards.api.payloads.SortOrder;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SortOrderValidator implements ConstraintValidator<SortOrderConstrain,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || Arrays.stream(SortOrder.values()).map(SortOrder::getOrder).anyMatch(so->so.equalsIgnoreCase(value));
    }
}
