package me.kjs.mall.product.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class ProductStockNotEnoughException extends BadRequestException {

    public ProductStockNotEnoughException() {
        super(ExceptionStatus.PRODUCT_STOCK_NOT_ENOUGH);
    }

}
