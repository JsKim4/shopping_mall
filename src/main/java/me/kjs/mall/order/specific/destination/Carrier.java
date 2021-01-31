package me.kjs.mall.order.specific.destination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum Carrier implements EnumType {
    CJ("CJ 대한통운", "CJ 대한통운", "kr.cjlogistics");
    private final String description;
    private final String name;
    private final String code;
}
