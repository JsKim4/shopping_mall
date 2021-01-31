package me.kjs.mall.order.payment;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.configs.properties.PaymentProperties;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentApproveRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_approve_request_id")
    private Long id;
    private String tradeId;
    private String authToken;
    private String paymentPrice;
    private LocalDateTime editDate;
    private String signData;

    public static PaymentApproveRequest createPaymentApproveRequest(PaymentCertificationResponse paymentCertificationResponse) {
        LocalDateTime editDate = LocalDateTime.now();
        String authToken = paymentCertificationResponse.getAuthToken();
        return PaymentApproveRequest.builder()
                .tradeId(paymentCertificationResponse.getTradeId())
                .authToken(paymentCertificationResponse.getAuthToken())
                .paymentPrice(paymentCertificationResponse.getPaymentPrice())
                .editDate(editDate)
                .signData(createSignData(authToken, editDate, paymentCertificationResponse.getPaymentPriceFormatInteger()))
                .build();
    }

    private static String createSignData(String authToken, LocalDateTime editDate, int paymentPrice) {
        String msg = authToken +
                PaymentProperties.getMerchantId() + paymentPrice +
                DateTimeUtil.formatToYYMMDDHHmmss(editDate) +
                PaymentProperties.getMerchantKey();
        String hex = DigestUtils.sha256Hex(msg);
        return hex;
    }

    public String getMerchantId() {
        return PaymentProperties.getMerchantId();
    }

    public String getFormatEditDate() {
        return DateTimeUtil.formatToYYMMDDHHmmss(editDate);
    }


}
