package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableExchangeRequestException extends BadRequestException {
    public NotAvailableExchangeRequestException() {
        super(ExceptionStatus.NOT_AVAILABLE_EXCHANGE_REQUEST);
    }
}
