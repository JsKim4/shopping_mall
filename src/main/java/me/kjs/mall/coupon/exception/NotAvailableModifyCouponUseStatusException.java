package me.kjs.mall.coupon.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;


public class NotAvailableModifyCouponUseStatusException extends BadRequestException {
    public NotAvailableModifyCouponUseStatusException() {
        super(ExceptionStatus.NOT_AVAILABLE_MODIFY_COUPON_USE_STATUS);
    }
}
