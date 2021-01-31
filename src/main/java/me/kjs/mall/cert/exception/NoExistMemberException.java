package me.kjs.mall.cert.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NoExistMemberException extends BadRequestException {

    public NoExistMemberException() {
        super(ExceptionStatus.NO_EXIST_MEMBER);
    }
}
