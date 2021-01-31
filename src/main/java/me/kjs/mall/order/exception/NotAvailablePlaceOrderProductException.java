package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailablePlaceOrderProductException extends BadRequestException {
    public NotAvailablePlaceOrderProductException(String orderProductCode, String reason) {
        super(ExceptionStatus.NOT_AVAILABLE_PLACE_ORDER_PRODUCT, ExceptionStatus.NOT_AVAILABLE_PLACE_ORDER_PRODUCT.getMessage().replace("{orderProductCode}", orderProductCode).replace("{reason}", reason));
    }
}
