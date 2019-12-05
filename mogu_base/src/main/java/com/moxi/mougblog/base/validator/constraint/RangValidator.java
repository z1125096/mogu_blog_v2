package com.moxi.mougblog.base.validator.constraint;

import com.moxi.mougblog.base.validator.Messages;
import com.moxi.mougblog.base.validator.annotion.Range;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 陌溪
 * @date 2019年12月4日13:17:03
 */
public class RangValidator implements ConstraintValidator<Range, String> {
    private long min;
    private long max;
    private final int DEFAULT_MAX = 11;

    @Override
    public void initialize(Range constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || StringUtils.isBlank(value)) {
            return false;
        }
        // 限制长度最大11
        if (value.length() > DEFAULT_MAX) {
            String template = String.format(Messages.CK_RANG_MESSAGE_LENGTH_TYPE, value);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(template).addConstraintViolation();
            return false;
        }
        // 是否可数字化
        if (!StringUtils.isNumeric(value)) {
            String template = String.format(Messages.CK_NUMERIC_TYPE, value);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(template).addConstraintViolation();
            return false;
        }
        long l = Long.parseLong(value);
        return l >= min && l <= max;
    }
}
