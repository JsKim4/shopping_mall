package me.kjs.mall.payment.nicepay;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.payment.OrderPayment;
import me.kjs.mall.order.payment.PaymentApproveRequest;

@Getter
@Builder
public class PaymentApproveRequestDto {
    private String tID;
    private String authToken;
    private String mid;
    private String amt;
    private String ediDate;
    private String signData;
    private String charSet;
    private String nextApproveUrl;
    private String netCancelUrl;

    public static PaymentApproveRequestDto orderPaymentApproveRequestToDto(OrderPayment orderPayment) {
        PaymentApproveRequest paymentApproveRequest = orderPayment.getPaymentApproveRequest();
        return PaymentApproveRequestDto.builder()
                .tID(paymentApproveRequest.getTradeId())
                .authToken(paymentApproveRequest.getAuthToken())
                .mid(paymentApproveRequest.getMerchantId())
                .amt(paymentApproveRequest.getPaymentPrice())
                .ediDate(paymentApproveRequest.getFormatEditDate())
                .signData(paymentApproveRequest.getSignData())
                .nextApproveUrl(orderPayment.getNextApproveUrl())
                .netCancelUrl(orderPayment.getNetCancelUrl())
                .charSet("utf-8")
                .build();

    }
}
