package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotEqualsProductTypeException extends BadRequestException {
    public NotEqualsProductTypeException() {
        super(ExceptionStatus.NOT_EQUALS_PRODUCT_TYPE);
    }

}
