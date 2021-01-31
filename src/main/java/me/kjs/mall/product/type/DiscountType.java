package me.kjs.mall.product.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@RequiredArgsConstructor
@Getter
public enum DiscountType implements EnumType {
    NONE("할인 타입 지정되지 않음"),
    FLAT_RATE("정액 할인"),
    PERCENT("정률 할인");

    private final String description;

}
