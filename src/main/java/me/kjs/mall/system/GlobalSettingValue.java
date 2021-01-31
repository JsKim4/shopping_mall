package me.kjs.mall.system;

import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.system.dto.AppInitValueDto;
import me.kjs.mall.system.seller.GlobalSeller;
import me.kjs.mall.system.seller.Seller;
import me.kjs.mall.system.version.AppVersion;
import me.kjs.mall.system.version.GlobalVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalSettingValue {

    private static List<GlobalVersion> globalVersions = new ArrayList<>();

    private static GlobalSeller globalSeller;


    public static List<GlobalVersion> getGlobalVersions() {
        return globalVersions;
    }

    public static AppInitValueDto getGlobalValue(String packageName) {
        GlobalVersion version = getVersion(packageName);
        GlobalSeller seller = getSeller();
        return new AppInitValueDto(version, seller);
    }

    public static GlobalVersion getVersion(String packageName) {
        return globalVersions.stream().filter(g -> g.getPackageName().equals(packageName)).findFirst().orElseThrow(NoExistIdException::new);
    }

    public static GlobalSeller getSeller() {
        return globalSeller;
    }

    public static void initGlobalVersion(List<AppVersion> versions) {
        globalVersions = versions.stream().map(GlobalVersion::versionToGlobalVersion).collect(Collectors.toList());
    }

    public static void initGlobalSeller(Seller seller) {
        globalSeller = GlobalSeller.sellerToGlobalSeller(seller);
    }

}
