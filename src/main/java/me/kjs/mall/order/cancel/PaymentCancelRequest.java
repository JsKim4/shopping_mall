package me.kjs.mall.order.cancel;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.CodeGeneratorUtil;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.payment.OrderPayment;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancelRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_cancel_id")
    private Long id;

    private String tradeId;
    private String merchantId;
    private String cancelCode;
    private int cancelPrice;
    private String cancelMessage;
    @Enumerated(EnumType.STRING)
    private YnType partialCancelCode;
    private LocalDateTime editDate;
    private String signData;
    private String charSet;
    private String accountNo;
    private String accountName;
    private String bankCode;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paymentCancelRequest")
    private OrderCancel orderCancel;


    public static PaymentCancelRequest createPaymentCancelRequest(Order order, OrderCancel orderCancel) {
        OrderPayment orderPayment = order.getOrderPayment();
        PaymentCancelRequest paymentCancelRequest = PaymentCancelRequest.builder()
                .tradeId(orderPayment.getTradeId())
                .merchantId(orderPayment.getMerchantId())
                .cancelCode(CodeGeneratorUtil.orderCancelCodeGenerate())
                .cancelPrice(orderCancel.getCancelPrice())
                .cancelMessage(orderCancel.getCancelCause())
                .editDate(LocalDateTime.now())
                .signData(orderPayment.getSignData())
                .charSet("utf-8")
                .orderCancel(orderCancel)
                .accountNo(orderCancel.getAccountNo())
                .accountName(orderCancel.getAccountName())
                .bankCode(orderCancel.getBankCode())
                .build();

        paymentCancelRequest.partialCancelCode = order.isOnlyOneOrder() ? YnType.N : YnType.Y;
        paymentCancelRequest.signData = createSignData(paymentCancelRequest.editDate, paymentCancelRequest.cancelPrice);

        return paymentCancelRequest;
    }

    private static String createSignData(LocalDateTime editDate, int cancelPrice) {
        String merchantId = PaymentProperties.getMerchantId();
        String edidate = DateTimeUtil.formatToYYMMDDHHmmss(editDate);
        String merchantKey = PaymentProperties.getMerchantKey();
        String msg =
                merchantId
                        + cancelPrice
                        + edidate
                        + merchantKey;
        String hex = DigestUtils.sha256Hex(msg);
        return hex;
    }

    public String getCancelUrl() {
        return PaymentProperties.getCancelApiUrl();
    }


    public String getFormatEditDate() {
        return DateTimeUtil.formatToYYMMDDHHmmss(editDate);
    }

    public int getFormatPartialCancelCode() {
        return partialCancelCode == YnType.Y ? 1 : 0;
    }

}
