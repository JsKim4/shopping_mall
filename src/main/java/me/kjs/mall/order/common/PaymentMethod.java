package me.kjs.mall.order.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod implements EnumType {
    KAKAO("카카오"),
    NAVER("네이버"),
    VBANK("가상계좌"),
    /*BANK("은행"),
    SSG_BANK("SSG 은행"),*/
    CARD("카드")/*,
    GIFT_SSG("SSG 은행 선물"),
    GIFT_CULT("SSG 문화 상품권"),
    CMS_BANK("CMS 은행")*/;

    private final String description;
}
