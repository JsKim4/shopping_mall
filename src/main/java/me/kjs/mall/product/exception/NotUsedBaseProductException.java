package me.kjs.mall.product.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotUsedBaseProductException extends BadRequestException {
    public NotUsedBaseProductException() {
        super(ExceptionStatus.NOT_USED_BASE_PRODUCT);
    }

}
