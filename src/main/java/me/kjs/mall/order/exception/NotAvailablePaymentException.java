package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailablePaymentException extends BadRequestException {
    public NotAvailablePaymentException() {
        super(ExceptionStatus.NOT_AVAILABLE_PAYMENT);
    }
}
