package me.kjs.mall.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonStatus implements EnumType {
    CREATED("생성된 상태"),
    USED("사용중인 상태"),
    UN_USED("사용 중지된 상태"),
    DELETED("삭제된 상태");
    private final String description;
}
