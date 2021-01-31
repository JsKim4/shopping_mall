package me.kjs.mall.common.exception;

public final class NoExistIdException extends StatusException {

    public NoExistIdException() {
        super(ExceptionStatus.NOT_FOUND_EXCEPTION);
    }

}
