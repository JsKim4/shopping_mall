package me.kjs.mall.order.payment;

import lombok.*;
import me.kjs.mall.common.util.CodeGeneratorUtil;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.payment.dto.PaymentVirtualBankNotifyDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;
import me.kjs.mall.payment.nicepay.PaymentCertificationResponseDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_payment_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String paymentCode;
    private int totalOriginalPrice;
    private int usePoint;
    private int totalDiscountPrice;
    private int totalDeliveryFee;
    private int totalPrice;
    private int totalPaymentPrice;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime paymentAcceptDateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_certification_request_id")
    private PaymentCertificationRequest paymentCertificationRequest;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_certification_response_id")
    private PaymentCertificationResponse paymentCertificationResponse;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_approve_request_id")
    private PaymentApproveRequest paymentApproveRequest;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_approve_response_id")
    private PaymentApproveResponse paymentApproveResponse;

    public static OrderPayment createOrderPayment(List<OrderSpecific> orderSpecifics, PaymentMethod paymentMethod, int usePoint) {
        int totalOriginalPrice = 0;
        int totalDiscountPrice = 0;
        int totalDeliveryFee = 0;
        int totalPrice = 0;
        int productCount = 0;
        int totalPaymentPrice = 0;
        String paymentCode = CodeGeneratorUtil.paymentCodeGenerate();
        for (OrderSpecific orderSpecific : orderSpecifics) {
            totalOriginalPrice += orderSpecific.getSumOriginalPrice();
            totalDiscountPrice += orderSpecific.getSumDiscountPrice();
            totalDeliveryFee += orderSpecific.getDeliveryFee();
            totalPrice += orderSpecific.getSumPrice();
            productCount += orderSpecific.getProductCount();
            totalPaymentPrice += orderSpecific.getSumPaymentPrice();
        }
        String productName = orderSpecifics.get(0).getRepresentProductName();
        String goodsName = productName + (productCount == 1 ? "" : (" 외 " + (productCount - 1)) + "건");
        PaymentCertificationRequest paymentCertificationRequest = PaymentCertificationRequest
                .createPaymentCertificationRequest(totalPaymentPrice, paymentCode, goodsName, paymentMethod);

        OrderPayment orderPayment = OrderPayment.builder()
                .totalOriginalPrice(totalOriginalPrice)
                .usePoint(usePoint)
                .paymentMethod(paymentMethod)
                .paymentCode(paymentCode)
                .paymentCertificationRequest(paymentCertificationRequest)
                .totalDiscountPrice(totalDiscountPrice)
                .totalDeliveryFee(totalDeliveryFee)
                .totalPrice(totalPrice)
                .totalPaymentPrice(totalPaymentPrice)
                .build();
        return orderPayment;
    }


    public LocalDateTime getPaymentDateTime() {
        if (paymentApproveResponse == null)
            return null;
        return paymentApproveResponse.getAcceptDateTime();
    }

    public int getTotalPaymentPrice() {
        return totalPaymentPrice;
    }

    public String getGoodsName() {
        return paymentCertificationRequest.getGoodsName();
    }

    public String getMerchantId() {
        return paymentCertificationRequest.getMerchantId();
    }

    public String getFormatEditDate() {
        return paymentCertificationRequest.getFormatEditDate();
    }

    public String getSignData() {
        return paymentCertificationRequest.getSignData();
    }

    public String getReturnUrl() {
        return paymentCertificationRequest.getReturnUrl();
    }

    public String getTradeId() {
        if (paymentApproveRequest == null) {
            return null;
        }
        return paymentApproveRequest.getTradeId();
    }

    public PaymentCertificationResponse createPaymentCertificationResponse(PaymentCertificationResponseDto paymentCertificationResponseDto) {
        paymentCertificationResponse = PaymentCertificationResponse.createPaymentCertificationResponse(paymentCertificationResponseDto);
        return paymentCertificationResponse;
    }

    public PaymentApproveRequest createPaymentApproveRequest() {
        paymentApproveRequest = PaymentApproveRequest.createPaymentApproveRequest(paymentCertificationResponse);
        return paymentApproveRequest;
    }

    public String getCertificationRequestUrl() {
        return PaymentProperties.getCertificationRequestUrl();
    }

    public boolean isCertificationResponseSuccess() {
        return paymentCertificationResponse.isCertificationResponseSuccess();
    }


    public PaymentApproveResponse createPaymentApproveResponse(PaymentApproveResponseDto paymentApproveResponseDto) {
        paymentApproveResponse = PaymentApproveResponse.createPaymentApproveResponse(paymentApproveResponseDto, paymentMethod);
        return paymentApproveResponse;
    }

    public boolean isApproveResponseSuccess() {
        return paymentApproveResponse.isApproveResponseSuccess();
    }

    public String getNextApproveUrl() {
        return paymentCertificationResponse.getNextApproveUrl();
    }

    public String getNetCancelUrl() {
        return paymentCertificationResponse.getNetCancelUrl();
    }

    public int getTotalPaymentPriceExcludeDeliveryFee() {
        return totalPaymentPrice - totalDeliveryFee;
    }

    public String getFormattingVBankExpDate() {
        if (paymentMethod != PaymentMethod.VBANK) {
            return null;
        }
        return DateTimeUtil.formatToYYMMDDHHmm(paymentCertificationRequest.getVbankExpDate());
    }

    public String getBankCode() {
        if (paymentMethod != PaymentMethod.VBANK) {
            return null;
        }
        return paymentApproveResponse.getBankCode();
    }

    public String getBankName() {
        if (paymentMethod != PaymentMethod.VBANK) {
            return null;
        }
        return paymentApproveResponse.getBankName();
    }

    public String getBankNum() {
        if (paymentMethod != PaymentMethod.VBANK) {
            return null;
        }
        return paymentApproveResponse.getBankNum();
    }

    public LocalDateTime getBankExpiredDateTime() {
        if (paymentMethod != PaymentMethod.VBANK) {
            return null;
        }
        return paymentApproveResponse.getBankExpiredDateTime();
    }

    public String createSignatureByAuthToken(String authToken) {
        if (authToken == null)
            return "";
        return paymentCertificationRequest.createSignatureByAuthToken(authToken, totalPaymentPrice);
    }

    public void paymentAccept() {
        paymentAcceptDateTime = LocalDateTime.now();
    }

    public boolean isVirtualBankPaymentAccept(PaymentVirtualBankNotifyDto paymentVirtualBankNotifyDto) {
        return paymentMethod.name().equals(paymentVirtualBankNotifyDto.getPayMethod()) &&
                "4110".equals(paymentVirtualBankNotifyDto.getResultCode());
    }

    public String getPaymentMethodOnNicePay() {
        if (paymentMethod == PaymentMethod.VBANK) {
            return "VBANK";
        }
        if (paymentMethod == null) {
            return "";
        }
        return "CARD";
    }

    public PaymentVirtualBank getPaymentVirtualBank() {
        return paymentApproveResponse.getPaymentVirtualBank();
    }
}
