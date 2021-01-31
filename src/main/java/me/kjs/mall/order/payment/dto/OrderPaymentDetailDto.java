package me.kjs.mall.order.payment.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.payment.OrderPayment;

import java.time.LocalDateTime;

@Builder
@Getter
public class OrderPaymentDetailDto {

    private int paymentPrice;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDateTime;

    public static OrderPaymentDetailDto orderPaymentToDetailDto(OrderPayment orderPayment) {
        return OrderPaymentDetailDto.builder()
                .paymentDateTime(orderPayment.getPaymentDateTime())
                .paymentMethod(orderPayment.getPaymentMethod())
                .paymentPrice(orderPayment.getTotalPaymentPrice())
                .build();
    }
}
