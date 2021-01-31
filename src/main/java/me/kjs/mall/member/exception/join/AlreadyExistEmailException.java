package me.kjs.mall.member.exception.join;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistEmailException extends BadRequestException {
    public AlreadyExistEmailException() {
        super(ExceptionStatus.ALREADY_EXIST_EMAIL);
    }


}
