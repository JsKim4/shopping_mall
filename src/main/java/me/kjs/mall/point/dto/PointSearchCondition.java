package me.kjs.mall.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.point.PointState;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointSearchCondition implements QueryCondition {
    private PointState pointState;
    @Min(0)
    private int page;
    @Min(5)
    @Max(30)
    private int contents;
}
