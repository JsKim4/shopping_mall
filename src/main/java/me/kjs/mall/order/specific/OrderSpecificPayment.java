package me.kjs.mall.order.specific;

import lombok.*;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.specific.product.OrderProduct;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSpecificPayment {
    private int sumOriginalPrice;
    private int sumDiscountPrice;
    private int sumPrice;
    private int usePoint;
    private int deliveryFee;
    private int cancelAvailablePrice;
    @Enumerated(EnumType.STRING)
    private YnType deliveryFreeBenefit;

    public static OrderSpecificPayment createOrderSpecificPayment(List<OrderProduct> orderProducts, Integer usePoint) {

        OrderSpecificPayment orderSpecificPayment = OrderSpecificPayment.builder().build();

        for (OrderProduct orderProduct : orderProducts) {
            orderSpecificPayment.sumOriginalPrice += orderProduct.getSumOriginPrice();
            orderSpecificPayment.sumDiscountPrice += orderProduct.getSumDiscountPrice();
            orderSpecificPayment.sumPrice += orderProduct.getSumPrice();
        }
        int minDeliveryFee = Integer.MAX_VALUE;
        for (OrderProduct orderProduct : orderProducts) {
            int deliveryFee = orderProduct.calculateDeliveryFee(orderSpecificPayment.sumPrice);
            minDeliveryFee = Math.min(minDeliveryFee, deliveryFee);
            if (minDeliveryFee == 0) {
                break;
            }
        }
        orderSpecificPayment.deliveryFee = minDeliveryFee;
        if (minDeliveryFee == 0) {
            orderSpecificPayment.deliveryFreeBenefit = YnType.Y;
        } else {
            orderSpecificPayment.deliveryFreeBenefit = YnType.N;
        }
        orderSpecificPayment.cancelAvailablePrice = orderSpecificPayment.deliveryFee + orderSpecificPayment.sumPrice - usePoint;
        orderSpecificPayment.usePoint = usePoint;
        return orderSpecificPayment;
    }

    public int cancelAvailablePrice() {
        return cancelAvailablePrice;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public int getSumOriginPrice() {
        return sumOriginalPrice;
    }

    public int getSumDiscountPrice() {
        return sumDiscountPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public int getTotalPaymentPrice() {
        return deliveryFee + sumPrice - usePoint;
    }

    public void paymentCancelSuccess(OrderCancel orderCancel) {
        cancelAvailablePrice -= orderCancel.getCancelPrice();
    }
}
