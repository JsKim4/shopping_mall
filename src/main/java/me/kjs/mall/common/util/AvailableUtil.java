package me.kjs.mall.common.util;

import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableUtil {
    public static List isUsedFilter(Collection<? extends AvailableCheck> availableChecks) {
        return availableChecks.stream().filter(AvailableCheck::isUsed).collect(Collectors.toList());
    }

    public static List isAvailableFilter(Collection<? extends AvailableCheck> availableChecks) {
        return availableChecks.stream().filter(AvailableCheck::isAvailable).collect(Collectors.toList());
    }

    public static boolean hasRole(Member member, AccountRole accountRole) {
        if (member == null) {
            return false;
        }
        return member.hasRole(accountRole);
    }
}
