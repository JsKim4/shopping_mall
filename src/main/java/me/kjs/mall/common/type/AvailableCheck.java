package me.kjs.mall.common.type;

public interface AvailableCheck {


    // 접근 가능한 최고 사용자가 사용 가능한지 체크한다.
    boolean isAvailable();

    // 권한이 부족한 사용자가 사용 가능 한지 체크한다.
    boolean isUsed();
}
