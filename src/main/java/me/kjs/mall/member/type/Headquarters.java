package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Headquarters implements EnumType {
    BUSINESS_MANAGEMENT("경영관리 본부", "경영관리 본부"),
    INFORMATION_TECHNOLOGY("IT 본부", "IT 본부"),
    MARKETING("마케팅 본부", "마케팅 본부"),
    CUSTOMER_MANAGEMENT("고객관리 본부", "고객관리 본부"),
    LABORATORY("연구소", "연구소"),

    PRODUCT_OPERATION("상품운영 본부", "상품운영 본부"),

    INTERIOR("인테리어 본부", "인테리어 본부"),

    SALES("영업 본부", "영업 본부"),

    MEDICAL_NUTRITION_COUNSEL("MNC 본부", "MNC 본부");

    private final String description;
    private final String headquartersName;


    public List<CompanyRank> getCompanyRankList() {
        return Arrays.stream(CompanyRank.class.getEnumConstants())
                .filter(cr -> cr.isAvail(this))
                .collect(Collectors.toList());
    }
}
