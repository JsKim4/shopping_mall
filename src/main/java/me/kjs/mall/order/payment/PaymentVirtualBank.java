package me.kjs.mall.order.payment;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Builder
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PaymentVirtualBank {

    private String bankCode;
    private String bankName;
    private String bankNum;
    private String expDate;
    private String expTime;

    public static PaymentVirtualBank createPaymentVirtualBank(PaymentApproveResponseDto paymentApproveResponse) {
        log.info("========================================");
        log.info("VirtualBankInfo : {}", paymentApproveResponse);
        log.info("========================================");
        return PaymentVirtualBank.builder()
                .bankCode(paymentApproveResponse.getBankCode())
                .bankName(paymentApproveResponse.getBankName())
                .bankNum(paymentApproveResponse.getBankNum())
                .expDate(paymentApproveResponse.getExpDate())
                .expTime(paymentApproveResponse.getExpTime())
                .build();
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankNum() {
        return bankNum;
    }

    public LocalDateTime getBankExpiredDateTime() {
        try {
            return DateTimeUtil.formatYYYYMMDDHHMMSSToLocalDateTime(expDate + expTime);
        } catch (Exception e) {
            return null;
        }
    }
}
