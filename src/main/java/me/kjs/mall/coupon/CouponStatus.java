package me.kjs.mall.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum CouponStatus implements EnumType {

    ISSUE("발급"),
    USED("사용"),
    EXPIRED("만료");

    private final String description;

}

