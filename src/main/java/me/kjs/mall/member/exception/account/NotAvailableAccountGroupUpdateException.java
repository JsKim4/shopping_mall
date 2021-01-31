package me.kjs.mall.member.exception.account;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotAvailableAccountGroupUpdateException extends BadRequestException {
    public NotAvailableAccountGroupUpdateException() {
        super(ExceptionStatus.NOT_AVAILABLE_ACCOUNT_GROUP_UPDATE);
    }

}
