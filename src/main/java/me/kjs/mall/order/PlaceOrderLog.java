package me.kjs.mall.order;

import lombok.*;
import me.kjs.mall.common.BaseEntity;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "place_order_log")
public class PlaceOrderLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_order_log_id")
    private Long id;

    private String requestLog;

    public static PlaceOrderLog createLog(String requestLog) {
        return PlaceOrderLog.builder()
                .requestLog(requestLog)
                .build();
    }
}
