package me.kjs.mall.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum ReviewOrderType implements EnumType {
    CREATED("최신순"),
    SCORE_DESC("별점 높은 순");

    private final String description;
}
