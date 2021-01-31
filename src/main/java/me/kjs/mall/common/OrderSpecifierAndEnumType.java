package me.kjs.mall.common;

import com.querydsl.core.types.OrderSpecifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kjs.mall.common.type.EnumType;

@Getter
@AllArgsConstructor
public class OrderSpecifierAndEnumType {
    private OrderSpecifier orderSpecifier;
    private EnumType enumType;
}
