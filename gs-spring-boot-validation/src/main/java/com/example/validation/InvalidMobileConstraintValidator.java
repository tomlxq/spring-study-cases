package com.example.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.example.validation.constraints.InvalidMobile;

public class InvalidMobileConstraintValidator implements ConstraintValidator<InvalidMobile, String> {
    @Override
    public void initialize(InvalidMobile constraintAnnotation) {

    }

    /**
     * 0086-135****499只能输这样的电话号码
     * 
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为什么不使用String#split在于使用正则，正则保护不够
        // JDK 里的 java.util.StringTokenizer,类似于Enumeration API
        // Apache commons-lang StringUtils
        String[] parts = StringUtils.split(value, "-");
        if (ArrayUtils.getLength(parts) != 2) {
            return false;
        }
        String prefix = parts[0];
        String suffix = parts[1];
        boolean isValidPrefix = Objects.equals(prefix, "086") || Objects.equals(prefix, "0086");
        boolean isValidInter = StringUtils.isNumeric(suffix);
        return isValidPrefix && isValidInter;
    }
}
