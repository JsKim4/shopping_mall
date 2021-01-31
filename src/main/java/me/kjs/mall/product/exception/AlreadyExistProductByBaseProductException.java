package me.kjs.mall.product.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistProductByBaseProductException extends BadRequestException {
    public AlreadyExistProductByBaseProductException() {
        super(ExceptionStatus.ALREADY_EXIST_PRODUCT_BY_BASE_PRODUCT);
    }
}
