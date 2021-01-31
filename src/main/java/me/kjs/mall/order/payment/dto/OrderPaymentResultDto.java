package me.kjs.mall.order.payment.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.Order;

import java.util.List;

@Getter
@Builder
public class OrderPaymentResultDto {
    private Long orderId;
    private List<Long> orderSpecificId;
    private List<String> orderCodes;
    private OrderPaymentDetailDto orderPaymentDetail;
    private OrderPaymentVirtualBankResultDto virtualBankResult;

    public static OrderPaymentResultDto orderToPaymentResultDto(Order resultOrder) {
        return OrderPaymentResultDto.builder()
                .orderId(resultOrder.getId())
                .orderSpecificId(resultOrder.getOrderSpecificIds())
                .orderCodes(resultOrder.getOrderCodes())
                .virtualBankResult(OrderPaymentVirtualBankResultDto.orderPaymentToVirtualBankResultDto(resultOrder.getOrderPayment()))
                .orderPaymentDetail(OrderPaymentDetailDto.orderPaymentToDetailDto(resultOrder.getOrderPayment()))
                .build();
    }
}
