package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableDeliveryDoingException extends BadRequestException {
    public NotAvailableDeliveryDoingException() {
        super(ExceptionStatus.NOT_AVAILABLE_DELIVERY_DOING);
    }

}
