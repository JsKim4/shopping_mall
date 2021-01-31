package me.kjs.mall.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum PolicyType implements EnumType {
    TERMS_SERVICE("이용약관 동의"),
    MARKETING("마케팅 수신동의"),
    PRIVACY("개인정보 활용 동의");
    private final String description;
}
