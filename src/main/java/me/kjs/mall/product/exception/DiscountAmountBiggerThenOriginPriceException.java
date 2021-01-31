package me.kjs.mall.product.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class DiscountAmountBiggerThenOriginPriceException extends BadRequestException {
    public DiscountAmountBiggerThenOriginPriceException() {
        super(ExceptionStatus.DISCOUNT_AMOUNT_BIGGER_THEN_ORIGIN_PRICE);
    }
}
