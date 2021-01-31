package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotEnoughPointException extends BadRequestException {
    public NotEnoughPointException() {
        super(ExceptionStatus.NOT_ENOUGH_POINT);
    }
}
