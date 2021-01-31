package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableExchangeCancelException extends BadRequestException {
    public NotAvailableExchangeCancelException() {
        super(ExceptionStatus.NOT_AVAILABLE_EXCHANGE_CANCEL);
    }

}
