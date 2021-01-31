package me.kjs.mall.product.base.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.base.ProductProvisionType;
import me.kjs.mall.product.base.ProvisionNotice;

@Getter
@Builder
public class ProvisionNoticeDto {
    private ProductProvisionType provisionType;
    private String foodType;
    private String manufacturer;
    private String location;
    private String manufacturingDate;
    private String shelfLifeDate;
    private String capacityUnit;
    private String quantityUnit;
    private String rawMaterialContents;
    private String nutritionInfo;
    private String functionInfo;
    private String intakeNotice;
    private String noMedicineGuidance;
    private String certificationAssociation;
    private String certificationCode;
    private YnType gmoYn;
    private YnType importYn;
    private String csTel;

    public static ProvisionNoticeDto provisionNoticeToDto(ProvisionNotice provisionNotice) {
        return ProvisionNoticeDto.builder()
                .provisionType(provisionNotice.getProvisionType())
                .foodType(provisionNotice.getFoodType())
                .manufacturer(provisionNotice.getManufacturer())
                .location(provisionNotice.getLocation())
                .manufacturingDate(provisionNotice.getManufacturingDate())
                .shelfLifeDate(provisionNotice.getShelfLifeDate())
                .capacityUnit(provisionNotice.getCapacityUnit())
                .quantityUnit(provisionNotice.getQuantityUnit())
                .rawMaterialContents(provisionNotice.getRawMaterialContents())
                .nutritionInfo(provisionNotice.getNutritionInfo())
                .functionInfo(provisionNotice.getFunctionInfo())
                .intakeNotice(provisionNotice.getIntakeNotice())
                .noMedicineGuidance(provisionNotice.getNoMedicineGuidance())
                .certificationAssociation(provisionNotice.getCertificationAssociation())
                .certificationCode(provisionNotice.getCertificationCode())
                .gmoYn(provisionNotice.getGmoYn())
                .importYn(provisionNotice.getImportYn())
                .csTel(provisionNotice.getCsTel())
                .build();
    }
}
