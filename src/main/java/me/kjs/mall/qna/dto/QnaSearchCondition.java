package me.kjs.mall.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.common.type.YnType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaSearchCondition implements QueryCondition {
    private YnType queryCurrent;
    private YnType answer;
    private Long productId;
    @Min(0)
    private int page;
    @Min(5)
    @Max(100)
    private int contents;
}
