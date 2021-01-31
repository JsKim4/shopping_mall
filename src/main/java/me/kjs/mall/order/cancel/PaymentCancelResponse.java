package me.kjs.mall.order.cancel;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.constant.PaymentCodeConstant;
import me.kjs.mall.order.cancel.dto.PaymentCancelResponseDto;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancelResponse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_cancel_response_id")
    private Long id;
    private String resultCode;
    private String resultMessage;
    private String errorCode;
    private String errorMessage;
    private String cancelPrice;
    private String merchantId;
    private String cancelCode;
    private String signature;
    private String tradeId;
    private String payMethod;
    private String cancelDate;
    private String cancelTime;
    private String cancelNum;
    private String remainPrice;


    public static PaymentCancelResponse createPaymentCancelResponse(PaymentCancelResponseDto paymentCancelResponseDto) {
        return PaymentCancelResponse.builder()
                .resultCode(paymentCancelResponseDto.getResultCode())
                .resultMessage(paymentCancelResponseDto.getResultMsg())
                .errorCode(paymentCancelResponseDto.getErrorCd())
                .errorMessage(paymentCancelResponseDto.getErrorMsg())
                .cancelPrice(paymentCancelResponseDto.getCancelAmt())
                .merchantId(paymentCancelResponseDto.getMid())
                .cancelCode(paymentCancelResponseDto.getMoid())
                .signature(paymentCancelResponseDto.getSignature())
                .tradeId(paymentCancelResponseDto.getTID())
                .payMethod(paymentCancelResponseDto.getPayMethod())
                .cancelDate(paymentCancelResponseDto.getCancelDate())
                .cancelTime(paymentCancelResponseDto.getCancelTime())
                .cancelNum(paymentCancelResponseDto.getCancelNum())
                .remainPrice(paymentCancelResponseDto.getRemainAmt())
                .build();
    }

    public boolean isSuccess() {
        return PaymentCodeConstant.PAYMENT_CANCEL_SUCCESS_CODES.contains(resultCode);
    }

    public String getFailMessage() {
        return errorCode + " " + errorMessage;
    }
}

