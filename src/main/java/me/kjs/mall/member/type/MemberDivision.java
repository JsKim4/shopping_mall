package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum MemberDivision implements EnumType {
    EMPLOYEE("직원"),
    FRIEND("친구");
    private final String description;
}
