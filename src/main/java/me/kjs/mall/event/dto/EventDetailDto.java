package me.kjs.mall.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.event.Event;
import me.kjs.mall.event.EventStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDto {
    private Long eventId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private LocalDate endDate;
    private CommonStatus status;
    private List<String> contents;
    private EventStatus eventStatus;
    private int visitCount;

    public static EventDetailDto eventToDetailDto(Event event) {
        return EventDetailDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .thumbnailImage(event.getThumbnailImage())
                .beginDate(event.getBeginDate())
                .endDate(event.getEndDate())
                .status(event.getStatus())
                .contents(event.getContents())
                .eventStatus(event.getEventStatus())
                .visitCount(event.getVisitCount())
                .build();
    }
}
