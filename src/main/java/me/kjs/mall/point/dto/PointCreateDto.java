package me.kjs.mall.point.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.point.PointKind;
import me.kjs.mall.point.PointState;

@Getter
@Builder
public class PointCreateDto {
    private OrderProduct orderProduct;
    private Order order;
    private Member member;
    private int amount;
    private int realAmount;
    private PointState pointState;
    private PointKind pointKind;
    private String misc;

    public static PointCreateDto accumulatePointByOrderAccept(OrderProduct orderProduct, Member member) {
        return PointCreateDto.builder()
                .order(orderProduct.getOrder())
                .orderProduct(orderProduct)
                .member(member)
                .amount(orderProduct.getAccumulateExpectedPoint())
                .realAmount(orderProduct.getAccumulateExpectedPoint())
                .pointState(PointState.ACCUMULATE)
                .pointKind(PointKind.PRODUCT_BUY_ACCEPT_ACCUMULATE)
                .misc(orderProduct.getProductName())
                .build();
    }

    public static PointCreateDto orderUsePointByBuy(Order order, Member member) {
        return PointCreateDto.builder()
                .order(order)
                .member(member)
                .amount(order.getUsePoint())
                .realAmount(order.getUsePoint() * (-1))
                .pointState(PointState.USE)
                .pointKind(PointKind.PRODUCT_BUY_USE)
                .misc(order.getOrderName())
                .build();
    }

    public static PointCreateDto expiredPoint(PointSpecificAndAmountDto expiredPoint) {
        return PointCreateDto.builder()
                .member(expiredPoint.getPointSpecific().getMember())
                .amount(expiredPoint.getAmount())
                .realAmount(expiredPoint.getAmount() * (-1))
                .pointState(PointState.EXPIRED)
                .pointKind(PointKind.POINT_EXPIRED)
                .misc("포인트 기간 만료")
                .build();
    }

    public static PointCreateDto accumulatePointByOrderCancel(OrderSpecific orderSpecific, int usePoint) {
        return PointCreateDto.builder()
                .member(orderSpecific.getMember())
                .amount(usePoint)
                .realAmount(usePoint)
                .pointState(PointState.ACCUMULATE)
                .pointKind(PointKind.ORDER_CANCEL_ACCUMULATE)
                .misc(PointKind.ORDER_CANCEL_ACCUMULATE.getDescription())
                .build();
    }
}
