package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableVirtualBankPaymentAcceptException extends BadRequestException {
    public NotAvailableVirtualBankPaymentAcceptException() {
        super(ExceptionStatus.NOT_AVAILABLE_VIRTUAL_BANK_PAYMENT_ACCEPT);
    }
}
