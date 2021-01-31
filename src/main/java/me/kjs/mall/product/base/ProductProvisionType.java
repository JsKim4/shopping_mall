package me.kjs.mall.product.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum ProductProvisionType implements EnumType {

    HEALTHY_FOOD("건강기능식품"),
    PROCESSED_FOOD("가공식품");


    private final String description;
}
