package me.kjs.mall.member.social.exception;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class AlreadyExistSocialException extends BadRequestException {
    public AlreadyExistSocialException() {
        super(ExceptionStatus.ALREADY_EXIST_SOCIAL);
    }
}
