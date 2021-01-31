package me.kjs.mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.order.common.OrderState;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdminSearchCondition implements QueryCondition {
    private List<OrderState> orderState;
    @Min(0)
    private int page;
    @Min(1)
    @Max(30)
    private int contents;
    private LocalDate orderBeginDate;
    private LocalDate orderEndDate;
}
