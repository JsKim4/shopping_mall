package me.kjs.mall.common.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationErrorException extends RuntimeException {
    private final Errors errors;

    public ValidationErrorException(Errors errors) {
        this.errors = errors;
    }
}
