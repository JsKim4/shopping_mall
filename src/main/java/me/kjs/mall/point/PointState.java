package me.kjs.mall.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@RequiredArgsConstructor
@Getter
public enum PointState implements EnumType {
    ACCUMULATE("적립"),
    EXPIRED("만료"),
    USE("사용");
    private final String description;
}
