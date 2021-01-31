package me.kjs.mall.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YnType implements EnumType {
    Y("승인"),
    N("마승인");
    private final String description;
}
