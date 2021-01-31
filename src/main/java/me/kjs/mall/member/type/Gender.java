package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;


@Getter
@RequiredArgsConstructor
public enum Gender implements EnumType {

    FEMALE("여성"),
    MALE("남성");

    private final String description;

}
