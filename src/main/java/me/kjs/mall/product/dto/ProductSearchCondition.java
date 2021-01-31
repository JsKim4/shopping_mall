package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.product.util.ProductSortType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCondition implements QueryCondition {
    @Builder.Default
    private CommonStatus status = CommonStatus.USED;
    private Long categoryId;
    private String keyword;
    private String code;
    @Min(-1)
    @Builder.Default
    private int stock = -1;
    private ProductSortType productSortType;

    @Min(0)
    @Builder.Default
    private int page = 0;
    @Min(1)
    @Max(200)
    @Builder.Default
    private int contents = 10;

    public Long getCategoryId() {
        if (categoryId == null || categoryId == -1)
            return null;
        return categoryId;
    }
}
