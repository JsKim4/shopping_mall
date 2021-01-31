package me.kjs.mall.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@RequiredArgsConstructor
@Getter
public enum EventStatus implements EnumType {
    PROCESS("진행중"),
    END("종료"),
    WAIT("진행예정");
    private final String description;
}
