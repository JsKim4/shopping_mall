package me.kjs.mall.order.payment;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.constant.PaymentCodeConstant;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentApproveResponse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_approve_response_id")
    private Long id;
    private String resultCode;
    private String resultMessage;
    private String paymentPrice;
    private String merchantId;
    private String signature;
    private String goodsName;
    private String payMethod;
    private String authDate;
    private String authCode;
    private String paymentCode;
    private LocalDateTime acceptDateTime;
    @Embedded
    private PaymentVirtualBank paymentVirtualBank;

    public static PaymentApproveResponse createPaymentApproveResponse(PaymentApproveResponseDto paymentApproveResponseDto, PaymentMethod paymentMethod) {
        PaymentApproveResponse paymentApproveResponse = PaymentApproveResponse.builder()
                .resultCode(paymentApproveResponseDto.getResultCode())
                .resultMessage(paymentApproveResponseDto.getResultMsg())
                .paymentPrice(paymentApproveResponseDto.getAmt())
                .merchantId(paymentApproveResponseDto.getMID())
                .signature(paymentApproveResponseDto.getSignature())
                .acceptDateTime(DateTimeUtil.formatYYMMDDHHMMSSToLocalDateTime(paymentApproveResponseDto.getAuthDate()))
                .authDate(paymentApproveResponseDto.getAuthDate())
                .goodsName(paymentApproveResponseDto.getGoodsName())
                .authCode(paymentApproveResponseDto.getAuthCode())
                .payMethod(paymentApproveResponseDto.getPayMethod())
                .paymentCode(paymentApproveResponseDto.getMoid())
                .build();
        if (paymentMethod == PaymentMethod.VBANK) {
            paymentApproveResponse.paymentVirtualBank = PaymentVirtualBank.createPaymentVirtualBank(paymentApproveResponseDto);
        }
        return paymentApproveResponse;
    }

    public boolean isApproveResponseSuccess() {
        return PaymentCodeConstant.PAYMENT_APPROVE_SUCCESS_CODES.contains(resultCode);
    }

    public String getBankCode() {
        if (paymentVirtualBank == null)
            return null;
        return paymentVirtualBank.getBankCode();
    }

    public String getBankName() {
        if (paymentVirtualBank == null)
            return null;
        return paymentVirtualBank.getBankName();
    }

    public String getBankNum() {
        if (paymentVirtualBank == null)
            return null;
        return paymentVirtualBank.getBankNum();
    }

    public LocalDateTime getBankExpiredDateTime() {
        if (paymentVirtualBank == null)
            return null;
        return paymentVirtualBank.getBankExpiredDateTime();
    }
}
