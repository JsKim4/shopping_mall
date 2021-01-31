package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.kjs.mall.member.type.BaseRank.*;
import static me.kjs.mall.member.type.Headquarters.*;

@RequiredArgsConstructor
@Getter
public enum CompanyRank implements EnumType {
    ASSISTANT_MANAGER("주임", "주임", LEVEL1, Arrays.asList(Headquarters.class.getEnumConstants())),
    ADMINISTRATIVE_MANAGER("대리", "대리", LEVEL2, Arrays.asList(Headquarters.class.getEnumConstants())),
    PART_MANAGER("파트장", "파트장", LEVEL2, Collections.singletonList(MEDICAL_NUTRITION_COUNSEL)),
    SECTION_MANAGER("과장", "과장", LEVEL3, Arrays.asList(Headquarters.class.getEnumConstants())),
    TEAM_MANAGER("팀장", "팀장", LEVEL4, Arrays.asList(Headquarters.class.getEnumConstants())),
    DEPARTMENT_MANAGER("실장", "실장", LEVEL5, Arrays.asList(Headquarters.class.getEnumConstants())),
    SITE_MANAGER("소장", "소장", LEVEL5, Collections.singletonList(SALES)),
    DIVISION_MANAGER("본부장", "본부장", LEVEL6, Arrays.asList(BUSINESS_MANAGEMENT, INFORMATION_TECHNOLOGY, MARKETING, CUSTOMER_MANAGEMENT)),
    RESEARCH_DIRECTOR("연구소장", "연구소장", LEVEL6, Collections.singletonList(LABORATORY)),
    DIRECTOR("이사", "이사", LEVEL6, Arrays.asList(PRODUCT_OPERATION, INTERIOR, MEDICAL_NUTRITION_COUNSEL)),
    AUDITING_DIRECTOR("고문", "고문", LEVEL6, Collections.singletonList(PRODUCT_OPERATION)),
    MANAGING_DIRECTOR("상무이사", "상무이사", LEVEL6, Collections.singletonList(SALES));

    private final String description;

    private final String name;
    private final BaseRank rank;
    private final List<Headquarters> headquartersList;

    public boolean isAvail(Headquarters headquarters) {
        return headquartersList.contains(headquarters);
    }


    public int getMaximumPurchaseLimit() {
        return rank.getMaximumPurchaseLimit();
    }

    public int getRegistrationRestrictions() {
        return rank.getRegistrationRestrictions();
    }
}
