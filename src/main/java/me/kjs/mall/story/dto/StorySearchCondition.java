package me.kjs.mall.story.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.common.type.QueryCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorySearchCondition implements QueryCondition {
    private CommonStatus status = null;
    private PostStatus postStatus = null;
    @Min(0)
    private int page;
    @Min(1)
    @Max(30)
    private int contents;
}
