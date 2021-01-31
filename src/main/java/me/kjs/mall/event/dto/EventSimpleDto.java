package me.kjs.mall.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.event.Event;
import me.kjs.mall.event.EventStatus;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSimpleDto {
    private Long eventId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private LocalDate endDate;
    private CommonStatus status;
    private EventStatus eventStatus;
    private int visitCount;

    public static EventSimpleDto eventToSimpleDto(Event event) {
        return EventSimpleDto.builder()
                .beginDate(event.getBeginDate())
                .endDate(event.getEndDate())
                .thumbnailImage(event.getThumbnailImage())
                .status(event.getStatus())
                .title(event.getTitle())
                .eventId(event.getId())
                .eventStatus(event.getEventStatus())
                .visitCount(event.getVisitCount())
                .build();
    }

}
