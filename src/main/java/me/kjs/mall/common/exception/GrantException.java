package me.kjs.mall.common.exception;


public class GrantException extends StatusException {

    public GrantException(String message) {
        super(ExceptionStatus.GRANT_EXCEPTION, message);
    }
}
