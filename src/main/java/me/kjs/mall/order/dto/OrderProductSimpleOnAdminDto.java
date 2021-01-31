package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.product.OrderProduct;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderProductSimpleOnAdminDto {
    private Long orderProductId;
    private Long productId;
    private Long memberId;
    private Long orderSpecificId;
    private Long orderId;
    private String orderProductCode;
    private String orderCode;
    private OrderState orderProductState;
    private String productCode;
    private String productName;
    private int quantity;
    private String memberName;
    private String memberEmail;
    private String recipient;
    private LocalDateTime orderDateTime;

    public static OrderProductSimpleOnAdminDto orderProductToSimpleOnAdminDto(OrderProduct orderProduct) {
        return OrderProductSimpleOnAdminDto.builder()
                .orderProductId(orderProduct.getOrderProductId())
                .productId(orderProduct.getProductId())
                .memberId(orderProduct.getMemberId())
                .orderSpecificId(orderProduct.getOrderSpecificId())
                .orderId(orderProduct.getOrderId())
                .orderProductCode(orderProduct.getOrderItemCode())
                .orderCode(orderProduct.getOrderCode())
                .orderProductState(orderProduct.getOrderProductState())
                .productCode(orderProduct.getProductCode())
                .productName(orderProduct.getProductName())
                .quantity(orderProduct.getQuantity())
                .memberName(orderProduct.getMemberName())
                .memberEmail(orderProduct.getMemberEmail())
                .recipient(orderProduct.getRecipient())
                .orderDateTime(orderProduct.getCreatedDate())
                .build();
    }
}
