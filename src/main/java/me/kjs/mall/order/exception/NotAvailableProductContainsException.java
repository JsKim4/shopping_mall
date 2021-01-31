package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableProductContainsException extends BadRequestException {
    public NotAvailableProductContainsException() {
        super(ExceptionStatus.NOT_AVAILABLE_PRODUCT_CONTAINS);
    }

}
