package me.kjs.mall.member.social.exception;

import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.ExistStatusBodyException;
import me.kjs.mall.member.social.SocialType;

public class KakaoConnectionException extends ExistStatusBodyException {
    public KakaoConnectionException() {
        super(ExceptionStatus.KAKAO_CONNECTION, null, SocialType.KAKAO, "kakao_login_result");
    }
}
