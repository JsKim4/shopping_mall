package me.kjs.mall.member.exception.account;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableFreeException extends BadRequestException {
    public NotAvailableFreeException() {
        super(ExceptionStatus.NOT_AVAILABLE_FREE);
    }

}
