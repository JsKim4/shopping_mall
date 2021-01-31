package me.kjs.mall.product.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.QueryCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductSearchCondition implements QueryCondition {
    @Min(0)
    private int minPrice;
    @Min(0)
    private int maxPrice;
    private CommonStatus status;
    private String code;
    private String keyword;
    @Min(0)
    private int page;
    @Min(5)
    @Max(300)
    private int contents;
    private Long categoryId;
}
