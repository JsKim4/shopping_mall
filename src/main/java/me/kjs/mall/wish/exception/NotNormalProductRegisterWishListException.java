package me.kjs.mall.wish.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotNormalProductRegisterWishListException extends BadRequestException {

    public NotNormalProductRegisterWishListException() {
        super(ExceptionStatus.NOT_NORMAL_PRODUCT_REGISTER_WISH_LIST);
    }
}
