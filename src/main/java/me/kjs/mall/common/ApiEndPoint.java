package me.kjs.mall.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum ApiEndPoint implements EnumType {

    MAIN_BANNER_TOP("메인 베너 상위 엔드포인트", null, "/api/banners", "/api/banners"),
    MAIN_BANNER_CREATE("메인 베너 상위 생성", MAIN_BANNER_TOP, "", "/api/banners"),
    MAIN_BANNER_DELETE("메인 베너 상위 삭제", MAIN_BANNER_TOP, "/{mainBannerId}", "/api/banners/{mainBannerId}"),
    MAIN_BANNER_QUERY("메인 베너 상위 조회", MAIN_BANNER_TOP, "", "/api/banners");

    private final String description;
    private final ApiEndPoint parentEndPoint;
    private final String apiPoint;
    private final String realApiEndPoint;
}
