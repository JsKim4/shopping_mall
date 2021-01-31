package me.kjs.mall.payment.nicepay;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.payment.OrderPayment;

@Getter
@Builder
public class PaymentCertificationRequestDto {
    private String goodsName;
    private int amt;
    private String mID;
    private String ediDate;
    private String moid;
    private String signData;
    private String returnUrl;
    private PaymentMethod payMethod;
    private String certificationRequestUrl;
    private String certificationRequestWebviewUrl;
    private String vbankExpDate;

    public static PaymentCertificationRequestDto orderPaymentToPaymentCertificationRequestDto(OrderPayment orderPayment) {
        return PaymentCertificationRequestDto.builder()
                .goodsName(orderPayment.getGoodsName())
                .amt(orderPayment.getTotalPaymentPrice())
                .mID(orderPayment.getMerchantId())
                .ediDate(orderPayment.getFormatEditDate())
                .moid(orderPayment.getPaymentCode())
                .signData(orderPayment.getSignData())
                .returnUrl(orderPayment.getReturnUrl())
                .payMethod(orderPayment.getPaymentMethod())
                .certificationRequestUrl(orderPayment.getCertificationRequestUrl())
                .certificationRequestWebviewUrl(PaymentProperties.getCertificationRequestWebviewUrl())
                .vbankExpDate(orderPayment.getFormattingVBankExpDate())
                .build();

    }

}
