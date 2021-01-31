package me.kjs.mall.member.exception.login;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class NotFoundMemberByRefreshTokenException extends BadRequestException {
    public NotFoundMemberByRefreshTokenException() {
        super(ExceptionStatus.NOT_FOUND_MEMBER_BY_REFRESH_TOKEN);
    }

}
