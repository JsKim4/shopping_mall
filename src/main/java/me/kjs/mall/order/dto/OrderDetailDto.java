package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.payment.dto.OrderPaymentDetailDto;
import me.kjs.mall.order.specific.dto.OrderSpecificDetailDto;
import me.kjs.mall.payment.nicepay.PaymentCertificationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class OrderDetailDto {
    private Long orderId;
    private PaymentCertificationRequestDto paymentCertificationRequest;
    private OrderPaymentDetailDto orderPayment;
    private List<OrderSpecificDetailDto> orderSpecifics;


    public static OrderDetailDto orderToResultDetailDto(Order order) {
        return OrderDetailDto.builder()
                .orderId(order.getId())
                .paymentCertificationRequest(PaymentCertificationRequestDto.orderPaymentToPaymentCertificationRequestDto(order.getOrderPayment()))
                .orderPayment(OrderPaymentDetailDto.orderPaymentToDetailDto(order.getOrderPayment()))
                .orderSpecifics(order.getOrderSpecifics().stream().map(OrderSpecificDetailDto::orderSpecificToDetailDto).collect(Collectors.toList()))
                .build();
    }


}
