package me.kjs.mall.common.exception;

public class BadRequestException extends StatusException {
    public BadRequestException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }

    public BadRequestException(ExceptionStatus exceptionStatus, String message) {
        super(exceptionStatus, message);
    }

}
