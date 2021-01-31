package me.kjs.mall.partial.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class BestReviewSizeOverException extends BadRequestException {
    public BestReviewSizeOverException() {
        super(ExceptionStatus.BEST_REVIEW_SIZE_OVER);
    }
}
