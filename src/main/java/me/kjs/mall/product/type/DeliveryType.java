package me.kjs.mall.product.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum DeliveryType implements EnumType {
    FREE("무료"),
    CONDITION("조건부 무료");

    private final String description;

    public int getFee(ProductDelivery productDelivery, int price) {
        if (productDelivery.getDeliveryType() == FREE) {
            return 0;
        } else if (this == CONDITION) {
            if (price >= productDelivery.getFeeCondition()) {
                return 0;
            } else {
                return productDelivery.getFee();
            }
        } else {
            return productDelivery.getFee();
        }
    }
}
