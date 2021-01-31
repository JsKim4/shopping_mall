package me.kjs.mall.notice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.notice.dto.NoticeSaveDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private LocalDateTime beginDateTime;
    private CommonStatus status;
    private String title;
    private String thumbnailImage;
    private String content;
    private int visitCount;

    public static Notice createNotice(NoticeSaveDto noticeSaveDto) {
        return Notice.builder()
                .beginDateTime(noticeSaveDto.getBeginDate().atTime(0, 0))
                .status(CommonStatus.CREATED)
                .title(noticeSaveDto.getTitle())
                .thumbnailImage(noticeSaveDto.getThumbnailImage())
                .content(noticeSaveDto.getContent())
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

    public void updateNotice(NoticeSaveDto noticeSaveDto) {
        if (noticeSaveDto.getBeginDate() != null) {
            this.beginDateTime = noticeSaveDto.getBeginDate().atTime(0, 0);
        }
        if (CollectionTextUtil.isNotBlank(noticeSaveDto.getThumbnailImage())) {
            thumbnailImage = noticeSaveDto.getThumbnailImage();
        }
        if (CollectionTextUtil.isNotBlank(noticeSaveDto.getThumbnailImage())) {
            title = noticeSaveDto.getTitle();
        }
        if (CollectionTextUtil.isNotBlank(noticeSaveDto.getContent())) {
            content = noticeSaveDto.getContent();
        }

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
