package me.kjs.mall.common.exception;

import lombok.Getter;

@Getter
public class StatusException extends RuntimeException {
    private final int status;

    public StatusException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.status = exceptionStatus.getStatus();
    }

    public StatusException(ExceptionStatus exceptionStatus, String message) {
        super(message);
        this.status = exceptionStatus.getStatus();
    }
}
