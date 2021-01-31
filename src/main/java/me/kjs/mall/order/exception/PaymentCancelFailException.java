package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class PaymentCancelFailException extends BadRequestException {

    public PaymentCancelFailException(String message) {
        super(ExceptionStatus.PAYMENT_CANCEL_FAIL, message);
    }
}
