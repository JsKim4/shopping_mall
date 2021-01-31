package me.kjs.mall.payment.nicepay;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.cancel.PaymentCancelRequest;

@Getter
@Builder
public class PaymentCancelRequestDto {
    private String tID;
    private String mID;
    private String moid;
    private String cancelAmt;
    private String cancelMsg;
    private String partialCancelCode;
    private String ediDate;
    private String signData;
    private String charSet;
    private String cancelUrl;
    private String refundAcctNo;
    private String refundBankCd;
    private String refundAcctNm;

    public static PaymentCancelRequestDto paymentCancelRequestToDto(PaymentCancelRequest paymentCancelRequest) {
        return PaymentCancelRequestDto.builder()
                .tID(paymentCancelRequest.getTradeId())
                .mID(paymentCancelRequest.getMerchantId())
                .moid(paymentCancelRequest.getCancelCode())
                .cancelAmt(String.valueOf(paymentCancelRequest.getCancelPrice()))
                .cancelMsg(paymentCancelRequest.getCancelMessage())
                .partialCancelCode(String.valueOf(paymentCancelRequest.getFormatPartialCancelCode()))
                .ediDate(paymentCancelRequest.getFormatEditDate())
                .signData(paymentCancelRequest.getSignData())
                .charSet(paymentCancelRequest.getCharSet())
                .cancelUrl(paymentCancelRequest.getCancelUrl())
                .refundAcctNo(paymentCancelRequest.getAccountNo())
                .refundAcctNm(paymentCancelRequest.getAccountName())
                .refundBankCd(paymentCancelRequest.getBankCode())
                .build();
    }
}
