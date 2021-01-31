package me.kjs.mall.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.event.EventStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchCondition implements QueryCondition {
    private CommonStatus status;
    private EventStatus eventStatus;
    @Min(0)
    private int page;
    @Min(1)
    @Max(30)
    private int contents;


}
