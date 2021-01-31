package me.kjs.mall.partial.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotRegisterBestReviewProductException extends BadRequestException {
    public NotRegisterBestReviewProductException() {
        super(ExceptionStatus.NOT_REGISTER_BEST_REVIEW_PRODUCT);
    }
}
