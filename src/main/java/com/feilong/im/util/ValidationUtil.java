package com.feilong.im.util;

import com.feilong.im.enums.MessageErrorEnum;
import com.feilong.im.exception.NettyClientException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验工具类
 * @author cfl 2026/04/09
 */
public class ValidationUtil {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = factory.getValidator();
        }
    }

    /**
     * 校验对象,如果有错误则抛出异常
     * @param obj 待校验对象
     * @param <T> 对象类型
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(obj);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new NettyClientException("参数校验失败: " + errorMsg, MessageErrorEnum.CLIENT_PARAM_ERROR);
        }
    }
}
