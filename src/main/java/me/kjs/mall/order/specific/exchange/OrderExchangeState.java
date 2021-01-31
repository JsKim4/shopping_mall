package me.kjs.mall.order.specific.exchange;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum OrderExchangeState implements EnumType {
    NONE("교환 상태가 아님"),
    EXCHANGE_REQUEST("교환 요청 상태"),
    EXCHANGE_REJECT("교환 거절 상태"),
    EXCHANGE_RECEPTION("교환 허가 상태"),
    EXCHANGE_CHECK("교환 확인 상태"),
    EXCHANGE_CHECK_REJECT("교환 확인 후 거절 상태"),
    EXCHANGE_ACCEPT("교환 승인 상태");

    private final String description;

}
