package com.logicea.cards.api.validations.sort;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.payloads.SortOrder;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;

public class SortFieldValidator implements ConstraintValidator<SortFieldConstraint,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || Arrays.stream(CardDTO.class.getDeclaredFields()).map(Field::getName).anyMatch(field -> field.equalsIgnoreCase(value));
    }
}
