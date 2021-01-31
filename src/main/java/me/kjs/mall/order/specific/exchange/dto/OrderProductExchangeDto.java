package me.kjs.mall.order.specific.exchange.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;

import java.util.List;

@Builder
@Getter
public class OrderProductExchangeDto {
    private Long orderProductId;
    private Long productId;
    private List<String> thumbnail;
    private String name;
    private int quantity;
    private int exchangeQuantity;
    private OrderExchangeState orderProductExchangeState;

    public static OrderProductExchangeDto orderProductToExchangeDto(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return OrderProductExchangeDto.builder()
                .orderProductId(orderProduct.getId())
                .productId(product.getId())
                .thumbnail(product.getBaseProductThumbnailImage())
                .name(orderProduct.getProductName())
                .quantity(orderProduct.getQuantity())
                .exchangeQuantity(orderProduct.getExchangeQuantity())
                .orderProductExchangeState(orderProduct.getOrderProductExchangeState())
                .build();
    }
}
