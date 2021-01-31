package me.kjs.mall.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpdateDto {
    @NotEmpty
    private String corporateRegistrationNumber;
    @NotEmpty
    private String companyLocation;
    @NotEmpty
    private String companyType;
    @NotEmpty
    private String netSaleReportNumber;
    @NotEmpty
    private String ceoName;
    @NotEmpty
    private String forwardingAddress;
    @NotEmpty
    private String returnAddress;
    @NotEmpty
    private String csCenterTel;
    private Integer deliveryFee;
    private Integer deliveryCondition;
    private Integer returnFee;
    @NotEmpty
    private String companyFax;
}
