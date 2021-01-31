package me.kjs.mall.member.exception;


import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistPhoneNumberException extends BadRequestException {
    public AlreadyExistPhoneNumberException() {
        super(ExceptionStatus.ALREADY_EXIST_PHONE_NUMBER);
    }
}
