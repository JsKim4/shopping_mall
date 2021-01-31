package me.kjs.mall.system.version;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.system.dto.VersionCreateDto;
import me.kjs.mall.system.dto.VersionUpdateDto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AppVersion extends BaseEntity {
    @Id
    private String packageName;
    private String os;
    private String latelyCode;
    private String latelyVersion;
    private String minVersion;
    private String minCode;

    public static AppVersion createAppVersion(VersionCreateDto versionCreateDto) {
        return AppVersion.builder()
                .packageName(versionCreateDto.getPackageName())
                .os(versionCreateDto.getOs())
                .latelyCode(versionCreateDto.getLatelyCode())
                .latelyVersion(versionCreateDto.getLatelyVersion())
                .minVersion(versionCreateDto.getMinVersion())
                .minCode(versionCreateDto.getMinCode())
                .build();
    }

    public void update(VersionUpdateDto versionUpdateDto) {
        if (CollectionTextUtil.isNotBlank(versionUpdateDto.getOs())) {
            os = versionUpdateDto.getOs();
        }
        if (CollectionTextUtil.isNotBlank(versionUpdateDto.getLatelyCode())) {
            latelyCode = versionUpdateDto.getLatelyCode();
        }
        if (CollectionTextUtil.isNotBlank(versionUpdateDto.getLatelyVersion())) {
            latelyVersion = versionUpdateDto.getLatelyVersion();
        }
        if (CollectionTextUtil.isNotBlank(versionUpdateDto.getMinVersion())) {
            minVersion = versionUpdateDto.getMinVersion();
        }
        if (CollectionTextUtil.isNotBlank(versionUpdateDto.getMinCode())) {
            minCode = versionUpdateDto.getMinCode();
        }
    }
}
