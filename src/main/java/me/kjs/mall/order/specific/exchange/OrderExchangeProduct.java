package me.kjs.mall.order.specific.exchange;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.order.specific.product.OrderProduct;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderExchangeProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_exchange_product_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_exchange_id")
    private OrderExchange orderExchange;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderExchangeProduct")
    private OrderProduct orderProduct;

    private int quantity;

    public static OrderExchangeProduct requestExchange(OrderProduct orderProduct, int quantity, OrderExchange orderExchange) {
        return OrderExchangeProduct.builder()
                .orderExchange(orderExchange)
                .orderProduct(orderProduct)
                .quantity(quantity)
                .build();
    }

    public void remove() {
        orderExchange = null;
        orderProduct = null;
    }
}
