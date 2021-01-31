package me.kjs.mall.product.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistProductCodeException extends BadRequestException {
    public AlreadyExistProductCodeException() {
        super(ExceptionStatus.ALREADY_EXIST_PRODUCT_CODE);
    }

}
