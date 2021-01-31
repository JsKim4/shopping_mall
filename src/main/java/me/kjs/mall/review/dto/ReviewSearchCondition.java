package me.kjs.mall.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.QueryCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSearchCondition implements QueryCondition {

    private Long productId;
    private CommonStatus status = null;
    @NotNull
    private ReviewOrderType reviewOrderType = ReviewOrderType.SCORE_DESC;
    @Min(0)
    private int page;
    @Min(5)
    @Max(30)
    private int contents;
}
