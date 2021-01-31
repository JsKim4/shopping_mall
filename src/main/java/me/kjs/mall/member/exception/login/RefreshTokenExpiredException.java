package me.kjs.mall.member.exception.login;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class RefreshTokenExpiredException extends BadRequestException {

    public RefreshTokenExpiredException() {
        super(ExceptionStatus.REFRESH_TOKEN_EXPIRED);
    }

}
