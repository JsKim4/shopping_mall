package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableDeliveryAcceptException extends BadRequestException {
    public NotAvailableDeliveryAcceptException() {
        super(ExceptionStatus.NOT_AVAILABLE_DELIVERY_ACCEPT);
    }

}
