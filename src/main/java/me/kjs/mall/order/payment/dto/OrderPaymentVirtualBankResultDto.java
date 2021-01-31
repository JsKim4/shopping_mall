package me.kjs.mall.order.payment.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.payment.OrderPayment;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderPaymentVirtualBankResultDto {
    private String bankCode;
    private String bankName;
    private String bankNum;
    private LocalDateTime bankExpiredDateTime;

    public static OrderPaymentVirtualBankResultDto orderPaymentToVirtualBankResultDto(OrderPayment orderPayment) {
        return OrderPaymentVirtualBankResultDto.builder()
                .bankCode(orderPayment.getBankCode())
                .bankName(orderPayment.getBankName())
                .bankNum(orderPayment.getBankNum())
                .bankExpiredDateTime(orderPayment.getBankExpiredDateTime())
                .build();
    }
}
