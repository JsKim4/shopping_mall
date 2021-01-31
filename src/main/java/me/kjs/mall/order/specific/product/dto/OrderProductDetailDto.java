package me.kjs.mall.order.specific.product.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;

import java.util.List;

@Getter
@Builder
public class OrderProductDetailDto {
    private Long orderProductId;
    private Long productId;
    private String name;
    private int quantity;
    private int price;
    private OrderExchangeState orderProductExchangeState;
    private OrderState orderProductState;
    private List<String> thumbnail;

    public static OrderProductDetailDto orderProductToDetailDto(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return OrderProductDetailDto.builder()
                .productId(product.getId())
                .orderProductId(orderProduct.getId())
                .name(product.getBaseProductName())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getSumPrice())
                .orderProductState(orderProduct.getOrderProductState())
                .orderProductExchangeState(orderProduct.getOrderProductExchangeState())
                .thumbnail(product.getBaseProductThumbnailImage())
                .build();
    }
}
