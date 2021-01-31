package me.kjs.mall.security;

import me.kjs.mall.member.Member;
import org.springframework.security.core.userdetails.User;

public class AccountAdapter extends User {

    private Member member;

    public AccountAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), member.getAuthorityRoles());
        this.member = member;

    }

    public Member getMember() {
        return member;
    }
}
