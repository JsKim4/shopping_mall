package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.product.OrderProductLog;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderProductLogHistoryDto {
    private OrderState postOrderState;
    private OrderState preOrderState;
    private LocalDateTime createDateTime;
    private String createdMemberName;


    public static OrderProductLogHistoryDto orderProductLogToHistoryDto(OrderProductLog orderProductLog) {
        return OrderProductLogHistoryDto.builder()
                .postOrderState(orderProductLog.getPostOrderProductState())
                .preOrderState(orderProductLog.getPreOrderProductState())
                .createDateTime(orderProductLog.getCreatedDate())
                .createdMemberName(orderProductLog.getCreateMemberName())
                .build();
    }
}
