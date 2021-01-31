package me.kjs.mall.member.social;

import me.kjs.mall.member.type.Gender;

public interface SocialRegisterInterface {
    String getName();

    String getId();

    Gender getGender();

    SocialType getSocialType();
}
