package me.kjs.mall.order.specific.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.specific.OrderSpecific;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderSpecificPaymentDetailDto {
    private int sumOriginalPrice;
    private int sumDiscountPrice;
    private int sumPrice;
    private int deliveryFee;
    private int sumPaymentPrice;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDateTime;

    public static OrderSpecificPaymentDetailDto orderSpecificPaymentToDetailDto(OrderSpecific orderSpecific) {
        return OrderSpecificPaymentDetailDto.builder()
                .sumOriginalPrice(orderSpecific.getSumOriginalPrice())
                .sumDiscountPrice(orderSpecific.getSumDiscountPrice())
                .sumPrice(orderSpecific.getSumPrice())
                .deliveryFee(orderSpecific.getDeliveryFee())
                .sumPaymentPrice(orderSpecific.getSumPaymentPrice())
                .paymentMethod(orderSpecific.getPaymentMethod())
                .paymentDateTime(orderSpecific.getPaymentDateTime())
                .build();
    }
}
