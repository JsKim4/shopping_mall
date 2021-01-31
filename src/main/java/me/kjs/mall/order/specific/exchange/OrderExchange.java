package me.kjs.mall.order.specific.exchange;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.order.specific.product.dto.OrderExchangeRequestDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderExchange extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_exchange_id")
    private Long id;

    private LocalDateTime receiptDateTime;

    private String exchangeCause;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private final List<String> causeImage = new ArrayList<>();

    public static OrderExchange requestExchange(OrderExchangeRequestDto orderExchangeRequestDto) {
        OrderExchange orderExchange = OrderExchange.builder()
                .causeImage(orderExchangeRequestDto.getCauseImage())
                .exchangeCause(orderExchangeRequestDto.getExchangeCause())
                .receiptDateTime(LocalDateTime.now())
                .build();
        return orderExchange;

    }
}
