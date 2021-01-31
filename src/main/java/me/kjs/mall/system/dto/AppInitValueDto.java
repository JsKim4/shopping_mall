package me.kjs.mall.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.system.seller.GlobalSeller;
import me.kjs.mall.system.version.GlobalVersion;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppInitValueDto {
    private GlobalVersion version;
    private GlobalSeller seller;
}
