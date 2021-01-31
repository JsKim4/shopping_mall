package me.kjs.mall.order.specific.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationDto;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.dto.OrderProductDetailDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderSpecificDetailDto {
    private Long orderSpecificId;
    private String orderSpecificCode;
    private OrderState orderState;
    private OrderExchangeState orderExchangeState;
    private OrderSpecificPaymentDetailDto orderSpecificPayment;
    private OrderDestinationDto orderDestination;
    private String deliveryApiUrl;
    private List<OrderProductDetailDto> orderProducts;

    public static OrderSpecificDetailDto orderSpecificToDetailDto(OrderSpecific orderSpecific) {
        return OrderSpecificDetailDto.builder()
                .orderDestination(OrderDestinationDto.orderDestinationToDto(orderSpecific.getOrderDestination()))
                .orderProducts(orderSpecific.getOrderProducts().stream().map(OrderProductDetailDto::orderProductToDetailDto).collect(Collectors.toList()))
                .orderSpecificPayment(OrderSpecificPaymentDetailDto.orderSpecificPaymentToDetailDto(orderSpecific))
                .orderState(orderSpecific.getOrderState())
                .orderExchangeState(orderSpecific.getOrderExchangeState())
                .orderSpecificId(orderSpecific.getId())
                .deliveryApiUrl(orderSpecific.getDeliveryApiUrl())
                .orderSpecificCode(orderSpecific.getOrderCode())
                .build();
    }
}
