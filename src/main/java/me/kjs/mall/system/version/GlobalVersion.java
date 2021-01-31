package me.kjs.mall.system.version;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalVersion {
    private String packageName;
    private String os;
    private String latelyCode;
    private String latelyVersion;
    private String minVersion;
    private String minCode;

    public static GlobalVersion versionToGlobalVersion(AppVersion v) {
        return GlobalVersion.builder()
                .packageName(v.getPackageName())
                .os(v.getOs())
                .latelyVersion(v.getLatelyVersion())
                .latelyCode(v.getLatelyCode())
                .minVersion(v.getMinVersion())
                .minCode(v.getMinCode())
                .build();
    }
}
