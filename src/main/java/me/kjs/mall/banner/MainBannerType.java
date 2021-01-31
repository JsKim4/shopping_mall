package me.kjs.mall.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum MainBannerType implements EnumType {

    NONE("지정되지 않음"),
    PRODUCT("상품"),
    EVENT("이벤트"),
    STORY("쇼핑 스토리");

    private final String description;
}
