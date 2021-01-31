package me.kjs.mall.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus implements EnumType {
    PROCESS("게시중"),
    WAIT("게시예정");

    private final String description;
}
