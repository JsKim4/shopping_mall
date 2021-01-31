package me.kjs.mall.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionCreateDto {
    @NotBlank
    private String latelyVersion;
    @NotBlank
    private String latelyCode;
    @NotBlank
    private String minVersion;
    @NotBlank
    private String minCode;
    @NotBlank
    private String os;
    @NotBlank
    private String packageName;

}
