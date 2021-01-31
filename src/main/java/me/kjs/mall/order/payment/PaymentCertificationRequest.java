package me.kjs.mall.order.payment;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.order.common.PaymentMethod;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCertificationRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_certification_id")
    private Long id;

    private String goodsName;
    private int paymentPrice;
    private String merchantId;
    private LocalDateTime editDate;
    private String signData;
    private String returnUrl;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private LocalDateTime vbankExpDate;

    public static PaymentCertificationRequest createPaymentCertificationRequest(int totalPaymentPrice, String paymentCode, String goodsName, PaymentMethod paymentMethod) {
        LocalDateTime editDate = LocalDateTime.now();
        PaymentCertificationRequest paymentCertificationRequest = PaymentCertificationRequest.builder()
                .goodsName(goodsName)
                .paymentPrice(totalPaymentPrice)
                .merchantId(PaymentProperties.getMerchantId())
                .editDate(editDate)
                .signData(createSignData(editDate, totalPaymentPrice))
                .returnUrl(PaymentProperties.getReturnUrl())
                .paymentMethod(paymentMethod)
                .build();
        if (paymentMethod == PaymentMethod.VBANK) {
            paymentCertificationRequest.vbankExpDate = LocalDateTime.now().plusDays(3);
        }
        return paymentCertificationRequest;
    }

    private static String createSignData(LocalDateTime editDate, int paymentPrice) {
        String merchantId = PaymentProperties.getMerchantId();
        String merchantKey = PaymentProperties.getMerchantKey();
        String edidate = DateTimeUtil.formatToYYMMDDHHmmss(editDate);
        String msg = edidate +
                merchantId
                + paymentPrice
                + merchantKey;
        return DigestUtils.sha256Hex(msg);
    }


    public String getFormatEditDate() {
        return DateTimeUtil.formatToYYMMDDHHmmss(editDate);
    }

    public String createSignatureByAuthToken(String authToken, int totalPaymentPrice) {
        String merchantId = PaymentProperties.getMerchantId();
        String merchantKey = PaymentProperties.getMerchantKey();
        String amt = String.valueOf(totalPaymentPrice);
        String msg = authToken + merchantId + amt + merchantKey;
        return DigestUtils.sha256Hex(msg);
    }
}
