package me.kjs.mall.member.social.exception;

import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.ExistStatusBodyException;
import me.kjs.mall.member.social.SocialRegisterDto;
import me.kjs.mall.member.social.SocialType;

public class KakaoNotRegisterUserException extends ExistStatusBodyException {
    public KakaoNotRegisterUserException(SocialRegisterDto socialRegisterDto) {
        super(ExceptionStatus.KAKAO_NOT_REGISTER_USER, socialRegisterDto, SocialType.KAKAO, "kakao_login_result");
    }
}
