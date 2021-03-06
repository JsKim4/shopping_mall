package me.kjs.mall.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.QueryCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonSearchCondition implements QueryCondition {
    @Min(0)
    private int page;
    @Min(1)
    @Max(100)
    private int contents;

}
