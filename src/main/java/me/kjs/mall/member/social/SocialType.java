package me.kjs.mall.member.social;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum SocialType implements EnumType {
    NAVER("네이버"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    FACEBOOK("페이스북");
    private final String description;
}