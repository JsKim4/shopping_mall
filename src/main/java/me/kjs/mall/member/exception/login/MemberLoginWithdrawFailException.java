package me.kjs.mall.member.exception.login;

import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;

public class MemberLoginWithdrawFailException extends BadRequestException {

    public MemberLoginWithdrawFailException() {
        super(ExceptionStatus.MEMBER_LOGIN_WITHDRAW_FAIL);
    }

}
