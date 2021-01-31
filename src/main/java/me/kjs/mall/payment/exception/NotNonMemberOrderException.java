package me.kjs.mall.payment.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotNonMemberOrderException extends BadRequestException {
    public NotNonMemberOrderException() {
        super(ExceptionStatus.NOT_NON_MEMBER_ORDER);
    }
}
