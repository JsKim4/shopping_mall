package me.kjs.mall.story;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.story.dto.StorySaveDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;
    private LocalDateTime beginDateTime;
    private CommonStatus status;
    private String title;
    private String thumbnailImage;
    @Column(length = 4000)
    private String content;
    private int visitCount;

    public static Story createStory(StorySaveDto storySaveDto) {
        return Story.builder()
                .beginDateTime(storySaveDto.getBeginDate().atTime(0, 0))
                .status(CommonStatus.CREATED)
                .title(storySaveDto.getTitle())
                .thumbnailImage(storySaveDto.getThumbnailImage())
                .content(storySaveDto.getContents())
                .visitCount(0)
                .build();
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED && beginDateTime.isBefore(LocalDateTime.now());
    }

    public void updateStory(StorySaveDto storySaveDto) {
        this.title = storySaveDto.getTitle();
        this.beginDateTime = storySaveDto.getBeginDate().atTime(0, 0);
        this.thumbnailImage = storySaveDto.getThumbnailImage();
        this.content = storySaveDto.getContents();
    }

    public void updateStatus(CommonStatus status) {
        this.status = status;
    }

    public void delete() {
        status = CommonStatus.DELETED;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public LocalDate getBeginDate() {
        return beginDateTime.toLocalDate();
    }

    public CommonStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public PostStatus getPostStatus() {
        if (status != CommonStatus.USED) {
            return PostStatus.WAIT;
        }
        if (beginDateTime.isBefore(LocalDateTime.now())) {
            return PostStatus.PROCESS;
        } else {
            return PostStatus.WAIT;
        }
    }

    public void visit() {
        visitCount++;
    }

    public int getVisitCount() {
        return visitCount;
    }
}
