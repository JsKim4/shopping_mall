package me.kjs.mall.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bank implements EnumType {

    KR("한국은행", "001"),
    KDB("산업은행", "002"),
    IBK("기업은행", "003"),
    KB("국민은행", "004"),
    SH("수협은행", "007"),
    SCL("수출입은행", "008"),
    NH("농협은행", "011"),
    UR("우리은행", "020"),
    SC("SC제일은행", "023"),
    KRCT("한국씨티은행", "027"),
    DG("대구은행", "031"),
    BS("부산은행", "032"),
    GJ("광주은행", "034"),
    JJ("제주은행", "035"),
    JB("전북은행", "037"),
    KN("경남은행", "039"),
    MG("새마을금고중앙회", "045"),
    SINHUB("신협", "048"),
    UCG("우체국", "071"),
    HANA("하나은행", "081"),
    SINHAN("신한은행", "088"),
    K_BANK("케이뱅크", "089"),
    KAKAO_BANK("카카오뱅크", "090");

    private final String description;
    private final String code;
}
