package me.kjs.mall.member.exception.account;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistAliasException extends BadRequestException {

    public AlreadyExistAliasException() {
        super(ExceptionStatus.ALREADY_EXIST_ALIAS);
    }

}
