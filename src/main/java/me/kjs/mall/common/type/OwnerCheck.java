package me.kjs.mall.common.type;

import me.kjs.mall.member.Member;

public interface OwnerCheck {
    boolean isOwner(Member member);
}
