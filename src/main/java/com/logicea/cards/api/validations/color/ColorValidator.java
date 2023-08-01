package com.logicea.cards.api.validations.color;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ColorValidator implements ConstraintValidator<ColorConstraint,String> {

    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6}$");
    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        String sub = color.substring(1);
        return color.startsWith("#") &&
                sub.length() == 6 &&
                pattern.matcher(sub).matches();
    }
}
