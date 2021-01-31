package me.kjs.mall.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum PointKind implements EnumType {
    PRODUCT_BUY_ACCEPT_ACCUMULATE("상품 구매 적립"),
    PRODUCT_FRIEND_BUY_ACCEPT_ACCUMULATE("친구 상품 구매 적립"),
    PRODUCT_BUY_USE("상품 구매 사용"),
    ADMIN_MEDIATE_USE("관리자 차감 포인트"),
    ADMIN_MEDIATE_ACCUMULATE("관리자 부여 포인트"),
    POINT_EXPIRED("포인트 만료"),
    ORDER_CANCEL_ACCUMULATE("주문 취소 적립");

    private final String description;
}
