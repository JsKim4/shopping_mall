package me.kjs.mall.product.util;

import lombok.Getter;
import me.kjs.mall.common.type.EnumType;

@Getter
public enum ProductSortType implements EnumType {
    DESC_PRICE("가격 내림차순"),
    ASC_PRICE("가격 오름차순"),
    NAME("이름순"),
    CREATE_DATE("생성순"),
    POPULARITY("인기순");

    private final String description;

    ProductSortType(String description) {
        this.description = description;
    }
}
