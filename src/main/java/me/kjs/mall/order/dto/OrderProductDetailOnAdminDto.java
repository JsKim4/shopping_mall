package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationDto;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderProductDetailOnAdminDto {
    private Long orderProductId;
    private Long orderSpecificId;
    private String orderCode;
    private String orderProductCode;
    private String productName;
    private OrderState orderProductState;
    private OrderExchangeState orderExchangeState;
    private String memberEmail;
    private int quantity;
    private int discountPrice;
    private int sumOriginPrice;
    private int paymentPrice;
    private OrderDestinationDto orderDestination;
    private List<OrderProductLogHistoryDto> orderProductLogHistories;


    public static OrderProductDetailOnAdminDto orderProductToDetailOnDto(OrderProduct orderProduct) {
        return OrderProductDetailOnAdminDto.builder()
                .orderSpecificId(orderProduct.getOrderSpecificId())
                .orderProductId(orderProduct.getId())
                .orderCode(orderProduct.getOrderCode())
                .orderProductCode(orderProduct.getOrderItemCode())
                .productName(orderProduct.getProductName())
                .orderProductState(orderProduct.getOrderProductState())
                .orderExchangeState(orderProduct.getOrderProductExchangeState())
                .memberEmail(orderProduct.getMemberEmail())
                .quantity(orderProduct.getQuantity())
                .discountPrice(orderProduct.getSumDiscountPrice())
                .sumOriginPrice(orderProduct.getSumOriginPrice())
                .paymentPrice(orderProduct.getSumPrice())
                .orderDestination(OrderDestinationDto.orderDestinationToDto(orderProduct.getOrderDestination()))
                .orderProductLogHistories(orderProduct.getOrderProductLog().stream().map(OrderProductLogHistoryDto::orderProductLogToHistoryDto).collect(Collectors.toList()))
                .build();
    }
}
