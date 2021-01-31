package me.kjs.mall.banner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.CommonStatus;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MainBanner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_banner_id")
    private Long id;

    private String pcImage;
    private String mobileImage;
    private int sortNumber;
    private String link;
    private Long itemId;
    @Enumerated(EnumType.STRING)
    private MainBannerType bannerType;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    public static MainBanner createMainBanner(MainBannerSaveDto mainBannerSaveDto) {
        return MainBanner.builder()
                .pcImage(mainBannerSaveDto.getPcImage())
                .mobileImage(mainBannerSaveDto.getMobileImage())
                .sortNumber(0)
                .link(mainBannerSaveDto.getLink())
                .itemId(mainBannerSaveDto.getItemId())
                .bannerType(mainBannerSaveDto.getBannerType())
                .status(CommonStatus.USED)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getPcImage() {
        return pcImage;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public String getLink() {
        return link;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public Long getItemId() {
        return itemId;
    }

    public MainBannerType getBannerType() {
        return bannerType;
    }

    public CommonStatus getStatus() {
        return status;
    }
}
