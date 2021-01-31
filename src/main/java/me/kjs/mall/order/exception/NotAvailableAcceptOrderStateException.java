package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableAcceptOrderStateException extends BadRequestException {
    public NotAvailableAcceptOrderStateException() {
        super(ExceptionStatus.NOT_AVAILABLE_ACCEPT_ORDER_STATE);
    }

}
