package me.kjs.mall.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionUpdateDto {
    private String os;
    private String latelyCode;
    private String latelyVersion;
    private String minVersion;
    private String minCode;

}
