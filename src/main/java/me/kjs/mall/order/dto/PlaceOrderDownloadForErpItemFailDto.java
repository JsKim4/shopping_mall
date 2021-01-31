package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.specific.destination.OrderDestination;
import me.kjs.mall.order.specific.product.OrderProduct;

@Getter
@Builder
public class PlaceOrderDownloadForErpItemFailDto {
    private Long orderSpecificId;
    private String orderCode;
    private String productCode;
    private String productName;
    private int quantity;
    private int price;
    private String recipient;
    private String tel1;
    private String tel2;
    private String address1;
    private String message;
    private String failCause;

    public static PlaceOrderDownloadForErpItemFailDto orderProductToPlaceOrderDownloadForErpFailDto(OrderProduct orderProduct, OrderDestination orderDestination, String failCause) {
        return PlaceOrderDownloadForErpItemFailDto.builder()
                .orderSpecificId(orderProduct.getOrderSpecificId())
                .orderCode(orderProduct.getOrderCode())
                .productCode(orderProduct.getProductCode())
                .productName(orderProduct.getProductName())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getSumOriginPrice())
                .recipient(orderProduct.getRecipient())
                .tel1(orderDestination.getTel1())
                .tel2(orderDestination.getTel2())
                .address1(orderDestination.getDestinationAddress())
                .message(orderDestination.getMessage())
                .failCause(failCause)
                .build();
    }
}