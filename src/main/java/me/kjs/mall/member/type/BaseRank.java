package me.kjs.mall.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BaseRank {
    LEVEL1(100000, 20),
    LEVEL2(100000, 20),
    LEVEL3(100000, 20),
    LEVEL4(100000, 20),
    LEVEL5(100000, 20),
    LEVEL6(100000, 20);

    final int maximumPurchaseLimit;
    final int registrationRestrictions;

}
