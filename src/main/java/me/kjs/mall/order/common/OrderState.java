package me.kjs.mall.order.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum OrderState implements EnumType {
    ORDER_CREATE("주문 생성"),
    PAYMENT_WAIT("결제 대기"),
    PAYMENT_ACCEPT("결제 승인"),
    PAYMENT_CANCEL("결제 취소"),
    PLACE_ORDER_WAIT("발주 대기"),
    DELIVERY_WAIT("배송 대기"),
    DELIVERY_DOING("배송중"),
    DELIVERY_ACCEPT("배송 완료"),
    ORDER_ACCEPT("구매 완료");

    private final String description;

    public static List<OrderState> getOrderStateOnAggregatePrice() {
        return Arrays.asList(PAYMENT_ACCEPT, DELIVERY_WAIT, DELIVERY_DOING, DELIVERY_ACCEPT, ORDER_ACCEPT);
    }
}