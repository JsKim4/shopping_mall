package me.kjs.mall.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptType implements EnumType {
    Y("동의"),
    N("거부");

    private final String description;
}
