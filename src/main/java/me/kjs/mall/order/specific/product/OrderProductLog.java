package me.kjs.mall.order.specific.product;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.order.common.OrderState;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderProductLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_log_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderState postOrderProductState;
    @Enumerated(EnumType.STRING)
    private OrderState preOrderProductState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    public static OrderProductLog createOrderProductLog(OrderProduct orderProduct) {
        return OrderProductLog.builder()
                .preOrderProductState(orderProduct.getPreOrderProductState())
                .postOrderProductState(orderProduct.getOrderProductState())
                .orderProduct(orderProduct)
                .build();
    }

    public String getCreateMemberName() {
        try {
            if (super.getCreatedBy() != null) {
                return getCreatedBy().getName();
            }
        } catch (Exception e) {
            return "System Error";
        }
        return "시스템";
    }
}
