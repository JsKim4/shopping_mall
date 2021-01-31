package me.kjs.mall.member.exception.account;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableBanException extends BadRequestException {

    public NotAvailableBanException() {
        super(ExceptionStatus.NOT_AVAILABLE_BAN);
    }

}
