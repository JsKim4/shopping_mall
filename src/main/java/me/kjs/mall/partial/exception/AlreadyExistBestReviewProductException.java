package me.kjs.mall.partial.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistBestReviewProductException extends BadRequestException {
    public AlreadyExistBestReviewProductException() {
        super(ExceptionStatus.ALREADY_EXIST_BEST_REVIEW_PRODUCT);
    }
}
