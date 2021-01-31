package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.specific.destination.OrderDestination;
import me.kjs.mall.order.specific.product.OrderProduct;

import java.time.LocalDate;

@Getter
@Builder
public class PlaceOrderDownloadForErpItemSuccessDto {
    private String 주문번호;
    private String 상품주문번호;
    private String 상품코드;
    private int 주문수량;
    private int 주문액;
    private String 수취인;
    private String 전화번호1;
    private String 전화번호2;
    private String 배송주소;
    private String 배송메시지;
    private int 결제금액;
    private int 사용포인트;
    private LocalDate 결제일자;

    /*private Long orderSpecificId;
    private String orderCode;
    private String productCode;
    private String productName;
    private int quantity;
    private int price;
    private String recipient;
    private String tel1;
    private String tel2;
    private String address1;
    private String message;*/

    public static PlaceOrderDownloadForErpItemSuccessDto orderProductToPlaceOrderDownloadForErpDto(OrderProduct orderProduct, OrderDestination orderDestination) {
        return PlaceOrderDownloadForErpItemSuccessDto.builder()
                .주문번호(orderProduct.getOrderCode())
                .상품주문번호(orderProduct.getOrderItemCode())
                .상품코드(orderProduct.getProductCode())
                .주문수량(orderProduct.getQuantity())
                .주문액(orderProduct.getSumPrice())
                .수취인(orderDestination.getRecipient())
                .전화번호1(orderDestination.getTel1())
                .전화번호2(orderDestination.getTel2())
                .배송주소(orderDestination.getDestinationAddress())
                .배송메시지(orderDestination.getMessage())
                .결제금액(orderProduct.getOrderSumPrice())
                .사용포인트(orderProduct.getUsePoint())
                .결제일자(orderProduct.getPaymentAcceptDate())
                .build();
    }
}
