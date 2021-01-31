package me.kjs.mall.member.exception.login;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class IllegalPasswordLoginFailedException extends BadRequestException {
    public IllegalPasswordLoginFailedException() {
        super(ExceptionStatus.ILLEGAL_PASSWORD_LOGIN_FAILED);
    }

}
