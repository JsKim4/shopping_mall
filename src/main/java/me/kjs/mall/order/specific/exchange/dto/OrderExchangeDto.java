package me.kjs.mall.order.specific.exchange.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class OrderExchangeDto {
    private Long orderSpecificId;
    private List<String> causeImage;
    private String exchangeCause;
    private OrderExchangeState orderExchangeState;
    private List<OrderProductExchangeDto> orderProductExchanges;

    public static OrderExchangeDto orderSpecificToOrderExchangeDto(OrderSpecific orderSpecific) {
        List<OrderProductExchangeDto> orderProductExchangeDtos = orderSpecific.getOrderProducts().stream().map(OrderProductExchangeDto::orderProductToExchangeDto).collect(Collectors.toList());
        return OrderExchangeDto.builder()
                .orderSpecificId(orderSpecific.getId())
                .causeImage(orderSpecific.getCauseImage())
                .exchangeCause(orderSpecific.getExchangeCause())
                .orderProductExchanges(orderProductExchangeDtos)
                .orderExchangeState(orderSpecific.getOrderExchangeState())
                .build();

    }
}
