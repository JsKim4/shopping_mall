package me.kjs.mall.payment.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableCancelOrderStateException extends BadRequestException {
    public NotAvailableCancelOrderStateException() {
        super(ExceptionStatus.NOT_AVAILABLE_CANCEL_ORDER_STATE);
    }


}
