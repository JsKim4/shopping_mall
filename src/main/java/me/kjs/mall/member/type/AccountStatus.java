package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@RequiredArgsConstructor
@Getter
public enum AccountStatus implements EnumType {
    ALLOW("허가됨"),
    RETIRED("퇴사 사용자"),
    BAN("이용 제한"),
    DELETE("지인 박탈"),
    WITHDRAW("탈퇴"),
    REQUEST("지인 등록 요청"),
    REJECT("지인 등록 요청 거절"),
    RE_REQUEST("지인 등록 재요청"),
    RE_REJECT("지인 등록 재요청 거절");

    final String description;
}
