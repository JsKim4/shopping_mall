package me.kjs.mall.sales;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.OrderSpecific;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plus_order_specific_id")
    private OrderSpecific plusOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minus_order_specific_id")
    private OrderSpecific minusOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int amount;

    @Enumerated(EnumType.STRING)
    private SalesType salesType;

    @Enumerated(EnumType.STRING)
    private OrderState orderSpecificType;

    public static SalesLog plusOrder(OrderSpecific orderSpecific) {
        return SalesLog.builder()
                .plusOrder(orderSpecific)
                .minusOrder(null)
                .order(orderSpecific.getOrder())
                .orderSpecificType(orderSpecific.getOrderState())
                .amount(orderSpecific.getSumPrice())
                .salesType(SalesType.PLUS)
                .build();
    }

    public static SalesLog minusOrder(OrderSpecific orderSpecific) {
        return SalesLog.builder()
                .plusOrder(null)
                .minusOrder(orderSpecific)
                .order(orderSpecific.getOrder())
                .orderSpecificType(orderSpecific.getOrderState())
                .amount(orderSpecific.getSumPrice())
                .salesType(SalesType.MINUS)
                .build();
    }

    public static SalesLog usePoint(Order order) {
        return SalesLog.builder()
                .plusOrder(null)
                .minusOrder(null)
                .order(order)
                .orderSpecificType(null)
                .amount(order.getUsePoint())
                .salesType(SalesType.USE_POINT)
                .build();
    }

    public static SalesLog refundPoint(Order order) {
        return SalesLog.builder()
                .plusOrder(null)
                .minusOrder(null)
                .order(order)
                .orderSpecificType(null)
                .amount(order.getUsePoint())
                .salesType(SalesType.REFUND_POINT)
                .build();
    }

    public enum SalesType {
        MINUS,
        PLUS,
        USE_POINT,
        REFUND_POINT
    }
}