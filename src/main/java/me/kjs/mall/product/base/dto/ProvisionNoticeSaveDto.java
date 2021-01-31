package me.kjs.mall.product.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.base.ProductProvisionType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionNoticeSaveDto {
    @NotNull
    private ProductProvisionType provisionType;
    @NotEmpty
    private String foodType;
    @NotEmpty
    private String manufacturer;
    @NotEmpty
    private String location;
    @NotEmpty
    private String manufacturingDate;
    @NotEmpty
    private String shelfLifeDate;
    @NotEmpty
    private String capacityUnit;
    @NotEmpty
    private String quantityUnit;
    @NotEmpty
    private String rawMaterialContents;
    @NotEmpty
    private String nutritionInfo;
    @NotEmpty
    private String functionInfo;
    @NotEmpty
    private String intakeNotice;
    @NotEmpty
    private String noMedicineGuidance;
    @NotEmpty
    private String certificationAssociation;
    @NotEmpty
    private String certificationCode;
    @NotNull
    private YnType gmoYn;
    @NotNull
    private YnType importYn;
    @NotEmpty
    private String csTel;
}



























































