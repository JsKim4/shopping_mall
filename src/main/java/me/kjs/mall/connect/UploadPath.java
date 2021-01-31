package me.kjs.mall.connect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum UploadPath implements EnumType {
    EXCHANGE("교환 관련 사진 업로드", "/mall_exchange"),
    BASE_PRODUCT("교환 관련 사진 업로드", "/mall_product"),
    STORY("교환 관련 사진 업로드", "/mall_story"),
    NOTICE("교환 관련 사진 업로드", "/mall_notice"),
    REVIEW("교환 관련 사진 업로드", "/mall_review"),
    EVENT("교환 관련 사진 업로드", "/mall_event");
    private final String description;
    private final String url;
}
