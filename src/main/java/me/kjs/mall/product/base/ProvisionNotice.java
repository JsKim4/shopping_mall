package me.kjs.mall.product.base;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.base.dto.ProvisionNoticeSaveDto;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProvisionNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provision_notice_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProductProvisionType provisionType;
    private String foodType;
    private String manufacturer;
    private String location;
    private String manufacturingDate;
    private String shelfLifeDate;
    @Embedded
    private CapacityUnit capacityUnit;
    @Embedded
    private QuantityUnit quantityUnit;
    private String rawMaterialContents;
    private String nutritionInfo;
    private String functionInfo;
    private String intakeNotice;
    private String noMedicineGuidance;
    @Embedded
    private ProvisionCertification certification;
    @Enumerated(EnumType.STRING)
    private YnType gmoYn;
    @Enumerated(EnumType.STRING)
    private YnType importYn;
    private String csTel;

    public void update(ProvisionNoticeSaveDto provisionNoticeSaveDto) {
        provisionType = provisionNoticeSaveDto.getProvisionType();
        foodType = provisionNoticeSaveDto.getFoodType();
        manufacturer = provisionNoticeSaveDto.getManufacturer();
        location = provisionNoticeSaveDto.getLocation();
        manufacturingDate = provisionNoticeSaveDto.getManufacturingDate();
        shelfLifeDate = provisionNoticeSaveDto.getShelfLifeDate();
        capacityUnit = CapacityUnit.initialize(provisionNoticeSaveDto.getCapacityUnit());
        quantityUnit = QuantityUnit.initialize(provisionNoticeSaveDto.getQuantityUnit());
        rawMaterialContents = provisionNoticeSaveDto.getRawMaterialContents();
        nutritionInfo = provisionNoticeSaveDto.getNutritionInfo();
        functionInfo = provisionNoticeSaveDto.getFunctionInfo();
        intakeNotice = provisionNoticeSaveDto.getIntakeNotice();
        noMedicineGuidance = provisionNoticeSaveDto.getNoMedicineGuidance();
        certification = ProvisionCertification.initialize(provisionNoticeSaveDto.getCertificationAssociation(), provisionNoticeSaveDto.getCertificationCode());
        gmoYn = provisionNoticeSaveDto.getGmoYn();
        importYn = provisionNoticeSaveDto.getImportYn();
        csTel = provisionNoticeSaveDto.getCsTel();
    }

    public static ProvisionNotice initialize() {
        return ProvisionNotice.builder()
                .provisionType(null)
                .foodType("")
                .manufacturer("")
                .location("")
                .manufacturingDate("")
                .shelfLifeDate("")
                .capacityUnit(CapacityUnit.initialize())
                .quantityUnit(QuantityUnit.initialize())
                .rawMaterialContents("")
                .nutritionInfo("")
                .functionInfo("")
                .intakeNotice("")
                .noMedicineGuidance("")
                .noMedicineGuidance("")
                .certification(ProvisionCertification.initialize())
                .gmoYn(YnType.N)
                .importYn(YnType.N)
                .csTel("")
                .build();
    }

    public ProductProvisionType getProvisionType() {
        return provisionType;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getLocation() {
        return location;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public String getShelfLifeDate() {
        return shelfLifeDate;
    }

    public String getCapacityUnit() {
        return capacityUnit.getCapacityUnit();
    }

    public String getQuantityUnit() {
        return quantityUnit.getQuantityUnit();
    }

    public String getRawMaterialContents() {
        return rawMaterialContents;
    }

    public String getNutritionInfo() {
        return nutritionInfo;
    }

    public String getFunctionInfo() {
        return functionInfo;
    }

    public String getIntakeNotice() {
        return intakeNotice;
    }

    public String getNoMedicineGuidance() {
        return noMedicineGuidance;
    }

    public String getCertificationAssociation() {
        return certification.getAssociation();
    }

    public String getCertificationCode() {
        return certification.getCode();
    }

    public YnType getGmoYn() {
        return gmoYn;
    }

    public YnType getImportYn() {
        return importYn;
    }

    public String getCsTel() {
        return csTel;
    }

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    /*@Setter
    @Getter*/
    public static class CapacityUnit {
        private int amount;
        private String unit;
        @Column(name = "capacity_unit_text")
        private String onlyText;
        @Enumerated(EnumType.STRING)
        @Column(name = "capacity_unit_text_yn")
        private YnType onlyTextYn;
        private String capacityUnit;

        public static CapacityUnit initialize() {
            return CapacityUnit.builder()
                    .amount(0)
                    .unit("개")
                    .build();
        }

        public static CapacityUnit initialize(String capacityUnit) {
            String tempString = capacityUnit.replaceAll(" ", "");
            if (!tempString.matches("^[0-9]{1,}[a-zA-Zㄱ-힣]{1,}")) {
                return CapacityUnit.builder()
                        .onlyText(capacityUnit)
                        .onlyTextYn(YnType.Y)
                        .build();
            }
            return CapacityUnit.builder()
                    .unit("")
                    .amount(0)
                    .onlyTextYn(YnType.N)
                    .build();
        }

        public String getCapacityUnit() {
            return onlyTextYn == YnType.Y ? onlyText : amount + unit;
        }
    }


    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuantityUnit {
        private int pieceAmount;
        private String pieceUnit;
        private int quantity;
        private String quantityUnit;
        @Column(name = "quantity_unit_text")
        private String onlyText;
        @Enumerated(EnumType.STRING)
        @Column(name = "quantity_unit_text_yn")
        private YnType onlyTextYn;

        public static QuantityUnit initialize() {
            return QuantityUnit.builder()
                    .pieceAmount(0)
                    .pieceUnit("g")
                    .quantity(0)
                    .quantityUnit("개")
                    .build();
        }

        public static QuantityUnit initialize(String quantityUnit) {
            String tempString = quantityUnit.replaceAll(" ", "");
            if (!tempString.matches("^[0-9]{1,}[a-zA-Zㄱ-힣]{1,}()")) {
                return QuantityUnit.builder()
                        .onlyText(quantityUnit)
                        .onlyTextYn(YnType.Y)
                        .build();
            }
            return QuantityUnit.builder()
                    .pieceUnit("")
                    .quantityUnit("")
                    .pieceAmount(0)
                    .quantity(0)
                    .onlyTextYn(YnType.N)
                    .build();
        }

        public String getQuantityUnit() {
            return onlyTextYn == YnType.Y ? onlyText : pieceAmount + pieceUnit + " x " + quantity + quantityUnit;
        }
    }

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvisionCertification {
        private String certificationAssociation;
        private String certificationCode;

        public static ProvisionCertification initialize() {
            return ProvisionCertification.builder()
                    .certificationAssociation("")
                    .certificationCode("")
                    .build();
        }

        public static ProvisionCertification initialize(String certificationAssociation, String certificationCode) {
            return ProvisionCertification.builder()
                    .certificationAssociation(certificationAssociation)
                    .certificationCode(certificationCode)
                    .build();
        }

        public String getCode() {
            return certificationCode;
        }

        public String getAssociation() {
            return certificationAssociation;
        }
    }
}
