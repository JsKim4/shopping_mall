package me.kjs.mall.destination.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class DestinationLimitOverException extends BadRequestException {
    public DestinationLimitOverException() {
        super(ExceptionStatus.DESTINATION_LIMIT_OVER);
    }
}
