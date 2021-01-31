package me.kjs.mall.order.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableOrderPurchaseLimitException extends BadRequestException {
    public NotAvailableOrderPurchaseLimitException(int maximumPurchase, int nowMonthOrderPrice, int nowOrderPrice) {
        super(ExceptionStatus.NOT_AVAILABLE_ORDER_PURCHASE_LIMIT,
                ExceptionStatus.NOT_AVAILABLE_ORDER_PURCHASE_LIMIT.getMessage()
                        .replace("${1}", String.valueOf(maximumPurchase))
                        .replace("${2}", String.valueOf(nowMonthOrderPrice))
                        .replace("${3}", String.valueOf(nowOrderPrice)));
    }
}
