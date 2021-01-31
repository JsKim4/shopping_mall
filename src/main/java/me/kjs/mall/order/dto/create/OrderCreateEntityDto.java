package me.kjs.mall.order.dto.create;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.interfaces.OrderCreatorFirst;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;

import java.util.List;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class OrderCreateEntityDto {

    private List<ProductAndQuantityDto> productAndQuantityDtoList;
    private List<OrderDestinationSaveDto> orderDestinationSaveDtos;
    private int usePoint;
    private PaymentMethod paymentMethod;

    public static OrderCreateEntityDto createOrderCreateEntity(List<ProductAndQuantityDto> productAndQuantityDtoList, OrderCreatorFirst orderCreateDto) {
        return OrderCreateEntityDto.builder()
                .productAndQuantityDtoList(productAndQuantityDtoList)
                .orderDestinationSaveDtos(orderCreateDto.getDestination())
                .paymentMethod(orderCreateDto.getPaymentMethod())
                .usePoint(orderCreateDto.getPoint())
                .build();
    }

}
