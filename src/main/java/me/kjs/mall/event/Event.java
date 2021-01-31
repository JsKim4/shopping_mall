package me.kjs.mall.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.event.dto.EventSaveDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String title;
    private String thumbnailImage;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> contents;
    private LocalDateTime beginDateTime;
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    private int visitCount;

    public static Event createEvent(EventSaveDto eventSaveDto) {
        return Event.builder()
                .beginDateTime(eventSaveDto.getBeginDate().atTime(0, 0))
                .endDateTime(eventSaveDto.getEndDate().atTime(0, 0).plusDays(1))
                .contents(eventSaveDto.getContents())
                .thumbnailImage(eventSaveDto.getThumbnailImage())
                .status(CommonStatus.CREATED)
                .title(eventSaveDto.getTitle())
                .visitCount(0)
                .build();
    }

    public void updateEvent(EventSaveDto eventSaveDto) {
        beginDateTime = eventSaveDto.getBeginDate().atTime(0, 0);
        endDateTime = eventSaveDto.getEndDate().atTime(0, 0).plusDays(1);
        contents = eventSaveDto.getContents();
        thumbnailImage = eventSaveDto.getThumbnailImage();
        title = eventSaveDto.getTitle();

    }

    public void updateStatus(CommonStatus used) {
        status = used;
    }

    public void delete() {
        status = CommonStatus.DELETED;
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public LocalDate getEndDate() {
        return endDateTime.toLocalDate().minusDays(1);
    }

    public LocalDate getBeginDate() {
        return beginDateTime.toLocalDate();
    }

    public Event loading() {
        for (String content : contents) {

        }
        return this;
    }

    public List<String> getContents() {
        return contents;
    }

    public EventStatus getEventStatus() {
        if (status == CommonStatus.CREATED) {
            return EventStatus.WAIT;
        }
        if (status == CommonStatus.UN_USED) {
            return EventStatus.END;
        }
        if (beginDateTime.isAfter(LocalDateTime.now())) {
            return EventStatus.WAIT;
        }
        if (endDateTime.isBefore(LocalDateTime.now())) {
            return EventStatus.END;
        }
        return EventStatus.PROCESS;
    }

    public void visit() {
        visitCount++;
    }

    public int getVisitCount() {
        return visitCount;
    }
}
