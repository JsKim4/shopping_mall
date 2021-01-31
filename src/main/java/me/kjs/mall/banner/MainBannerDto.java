package me.kjs.mall.banner;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.CommonStatus;

@Getter
@Builder
public class MainBannerDto {
    private Long mainBannerId;
    private String pcImage;
    private String mobileImage;
    private int sortNumber;
    private String link;
    private Long itemId;
    private MainBannerType bannerType;
    private CommonStatus status;

    public static MainBannerDto mainBannerToDto(MainBanner mainBanner) {
        return MainBannerDto.builder()
                .mainBannerId(mainBanner.getId())
                .pcImage(mainBanner.getPcImage())
                .mobileImage(mainBanner.getMobileImage())
                .sortNumber(mainBanner.getSortNumber())
                .link(mainBanner.getLink())
                .itemId(mainBanner.getItemId())
                .bannerType(mainBanner.getBannerType())
                .status(mainBanner.getStatus())
                .build();
    }
}
