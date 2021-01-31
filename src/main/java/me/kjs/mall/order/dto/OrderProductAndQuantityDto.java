package me.kjs.mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.specific.product.OrderProduct;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductAndQuantityDto {
    private OrderProduct orderProduct;
    private int quantity;

    public static OrderProductAndQuantityDto createOrdersExchangeProductAndQuantity(OrderProduct orderProduct, int quantity) {
        return OrderProductAndQuantityDto.builder()
                .orderProduct(orderProduct)
                .quantity(quantity)
                .build();
    }
}
