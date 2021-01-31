package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.OrderSpecific;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderSpecificSimpleDto {
    private Long orderSpecificId;
    private String orderSpecificCode;
    private String orderName;
    private LocalDate orderDate;
    private int sumPaymentPrice;
    private List<String> thumbnail;
    private OrderState orderState;
    private String deliveryApiUrl;

    public static OrderSpecificSimpleDto orderSpecificToSimpleDto(OrderSpecific orderSpecific) {
        return OrderSpecificSimpleDto.builder()
                .orderSpecificId(orderSpecific.getId())
                .orderSpecificCode(orderSpecific.getOrderCode())
                .orderName(orderSpecific.getOrderName())
                .orderDate(orderSpecific.getOrderDateTime().toLocalDate())
                .sumPaymentPrice(orderSpecific.getSumPaymentPrice())
                .thumbnail(orderSpecific.getThumbnail())
                .orderState(orderSpecific.getOrderState())
                .deliveryApiUrl(orderSpecific.getDeliveryApiUrl())
                .build();
    }
}
