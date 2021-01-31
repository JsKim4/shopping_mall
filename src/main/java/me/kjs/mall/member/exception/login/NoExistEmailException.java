package me.kjs.mall.member.exception.login;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NoExistEmailException extends BadRequestException {
    public NoExistEmailException() {
        super(ExceptionStatus.NO_EXIST_EMAIL);
    }

}
