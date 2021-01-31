package me.kjs.mall.member.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NoExistPhoneNumberException extends BadRequestException {
    public NoExistPhoneNumberException() {
        super(ExceptionStatus.NO_EXIST_PHONE_NUMBER);
    }
}
