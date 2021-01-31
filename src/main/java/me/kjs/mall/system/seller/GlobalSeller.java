package me.kjs.mall.system.seller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalSeller {
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


    public static GlobalSeller sellerToGlobalSeller(Seller seller) {
        return GlobalSeller.builder()
                .companyName(seller.getCompanyName())
                .corporateRegistrationNumber(seller.getCorporateRegistrationNumber())
                .companyLocation(seller.getCompanyLocation())
                .companyType(seller.getCompanyType())
                .netSaleReportNumber(seller.getNetSaleReportNumber())
                .ceoName(seller.getCeoName())
                .forwardingAddress(seller.getForwardingAddress())
                .returnAddress(seller.getReturnAddress())
                .csCenterTel(seller.getCsCenterTel())
                .deliveryFee(seller.getDeliveryFee())
                .deliveryCondition(seller.getDeliveryCondition())
                .returnFee(seller.getReturnFee())
                .companyFax(seller.getCompanyFax())
                .build();
    }
}
