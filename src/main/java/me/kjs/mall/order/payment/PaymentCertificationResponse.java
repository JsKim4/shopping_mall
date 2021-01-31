package me.kjs.mall.order.payment;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.constant.PaymentCodeConstant;
import me.kjs.mall.payment.nicepay.PaymentCertificationResponseDto;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCertificationResponse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_certification_response_id")
    private Long id;

    private String resultMessage;
    private String resultCode;
    private String authToken;
    private String payMethod;
    private String merchantId;
    private String paymentPrice;
    private String signature;
    private String tradeId;
    private String nextApproveUrl;
    private String netCancelUrl;

    public static PaymentCertificationResponse createPaymentCertificationResponse(PaymentCertificationResponseDto paymentCertificationResponseDto) {
        return PaymentCertificationResponse.builder()
                .resultMessage(paymentCertificationResponseDto.getAuthResultMsg())
                .resultCode(paymentCertificationResponseDto.getAuthResultCode())
                .authToken(paymentCertificationResponseDto.getAuthToken())
                .payMethod(paymentCertificationResponseDto.getPayMethod())
                .merchantId(paymentCertificationResponseDto.getMID())
                .paymentPrice(paymentCertificationResponseDto.getAmt())
                .signature(paymentCertificationResponseDto.getSignature())
                .tradeId(paymentCertificationResponseDto.getTxTid())
                .nextApproveUrl(paymentCertificationResponseDto.getNextAppURL())
                .netCancelUrl(paymentCertificationResponseDto.getNetCancelURL())
                .build();
    }

    public int getPaymentPriceFormatInteger() {
        return Integer.parseInt(paymentPrice);
    }

    public boolean isCertificationResponseSuccess() {
        return PaymentCodeConstant.PAYMENT_CERTIFICATION_SUCCESS_CODES.contains(resultCode);
    }
}
