package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum AccountRole implements EnumType {
    USER("일반 사용"),
    ACCOUNT_GROUP("계정 그룹 권한 관리"),
    BANNER("배너 관리"),
    MEMBER_ADD("회원 추가"),
    MEMBER_MODIFY("회원 변경"),
    MEMBER_READ("회원 조회"),
    MEMBER_DELETE("회원 삭제"),
    BASE_PRODUCT("상품 기본 정보"),
    PRODUCT("상품 정보"),
    ORDER("주문 관리"),
    EVENT("이벤트 관리"),
    STORY("쇼핑스토리 관리"),
    NOTICE("공지사항 관리"),
    COUPON("쿠폰 관리"),
    POINT("포인트 관리"),
    REVIEW("리뷰 관리"),
    QNA("QNA 관리"),
    ADMIN("최고 관리자");
    private final String description;
}
