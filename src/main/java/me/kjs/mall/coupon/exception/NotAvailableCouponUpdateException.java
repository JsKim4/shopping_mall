package me.kjs.mall.coupon.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableCouponUpdateException extends BadRequestException {
    public NotAvailableCouponUpdateException() {
        super(ExceptionStatus.Not_AVAILABLE_COUPON_UPDATE);
    }
}
