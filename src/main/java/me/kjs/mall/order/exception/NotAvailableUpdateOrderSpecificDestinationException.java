package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableUpdateOrderSpecificDestinationException extends BadRequestException {
    public NotAvailableUpdateOrderSpecificDestinationException() {
        super(ExceptionStatus.NOT_AVAILABLE_UPDATE_ORDER_SPECIFIC_DESTINATION);
    }

}
