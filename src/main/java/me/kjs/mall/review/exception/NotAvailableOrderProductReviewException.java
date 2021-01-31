package me.kjs.mall.review.exception;

import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.StatusException;

public class NotAvailableOrderProductReviewException extends StatusException {
    public NotAvailableOrderProductReviewException() {
        super(ExceptionStatus.NOT_AVAILABLE_ORDER_PRODUCT_REVIEW);
    }
}
