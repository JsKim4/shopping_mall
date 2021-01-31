package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableOrderAccountStatusException extends BadRequestException {
    public NotAvailableOrderAccountStatusException() {
        super(ExceptionStatus.NOT_AVAILABLE_ORDER_ACCOUNT_STATUS);
    }
}
