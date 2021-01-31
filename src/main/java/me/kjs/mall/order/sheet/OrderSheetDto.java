package me.kjs.mall.order.sheet;


import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderSheetDto {
    private List<OrderSheetProductDto> products;

    private PaymentSimpleDto paymentInfo;

    public static OrderSheetDto createOrderSheetDto(List<ProductAndQuantityDto> productAndQuantityDto) {
        List<OrderSheetProductDto> products = productAndQuantityDto.stream()
                .map(OrderSheetProductDto::productAndQuantityToOrderSheetProductDto).collect(Collectors.toList());
        PaymentSimpleDto paymentSimpleDto = PaymentSimpleDto.createPaymentSimpleDto(productAndQuantityDto);
        return OrderSheetDto.builder()
                .products(products)
                .paymentInfo(paymentSimpleDto)
                .build();
    }


    @Builder
    @Getter
    private static class PaymentSimpleDto {
        private int deliveryFee;
        private int originPrice;
        private int sumDiscountPrice;
        private int sumPrice;

        private int sumPaymentPrice;

        public static PaymentSimpleDto createPaymentSimpleDto(List<ProductAndQuantityDto> productAndQuantityDto) {
            PaymentSimpleDto paymentSimpleDto = PaymentSimpleDto.builder().build();
            for (ProductAndQuantityDto product : productAndQuantityDto) {
                paymentSimpleDto.originPrice += product.getSumOriginPrice();
                paymentSimpleDto.sumPrice += product.getSumPrice();
                paymentSimpleDto.sumDiscountPrice += product.getSumDiscountPrice();
            }
            final int sumPrice = paymentSimpleDto.sumPrice;

            int minDeliveryFee = Integer.MAX_VALUE;

            for (ProductAndQuantityDto product : productAndQuantityDto) {
                int deliveryFee = product.calculateDeliveryFee(sumPrice);
                if (deliveryFee == 0) {
                    minDeliveryFee = 0;
                    break;
                } else if (minDeliveryFee > deliveryFee) {
                    minDeliveryFee = deliveryFee;
                }
            }
            paymentSimpleDto.deliveryFee = minDeliveryFee;
            paymentSimpleDto.sumPaymentPrice = minDeliveryFee + sumPrice;
            return paymentSimpleDto;
        }
    }
}
