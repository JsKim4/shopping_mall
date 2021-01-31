package me.kjs.mall.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainBannerSaveDto {
    @NotBlank
    private String pcImage;
    @NotBlank
    private String mobileImage;
    private String link;
    private Long itemId;
    @NotNull
    private MainBannerType bannerType;
}
