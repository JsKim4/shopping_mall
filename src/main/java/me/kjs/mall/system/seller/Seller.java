package me.kjs.mall.system.seller;

import lombok.*;
import me.kjs.mall.system.dto.SellerUpdateDto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seller {
    @Id
    private String companyName;
    private String corporateRegistrationNumber;
    private String companyLocation;
    private String companyType;
    private String netSaleReportNumber;
    private String ceoName;
    private String forwardingAddress;
    private String returnAddress;
    private String csCenterTel;
    private Integer deliveryFee;
    private Integer deliveryCondition;
    private Integer returnFee;
    private String companyFax;

    public void update(SellerUpdateDto sellerUpdateDto) {
        corporateRegistrationNumber = sellerUpdateDto.getCorporateRegistrationNumber();
        companyLocation = sellerUpdateDto.getCompanyLocation();
        companyType = sellerUpdateDto.getCompanyType();
        netSaleReportNumber = sellerUpdateDto.getNetSaleReportNumber();
        ceoName = sellerUpdateDto.getCeoName();
        forwardingAddress = sellerUpdateDto.getForwardingAddress();
        returnAddress = sellerUpdateDto.getReturnAddress();
        csCenterTel = sellerUpdateDto.getCsCenterTel();
        deliveryFee = sellerUpdateDto.getDeliveryFee();
        deliveryCondition = sellerUpdateDto.getDeliveryCondition();
        returnFee = sellerUpdateDto.getReturnFee();
        companyFax = sellerUpdateDto.getCompanyFax();
    }
}
