package me.kjs.mall.notice.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.notice.Notice;

import java.time.LocalDate;

@Getter
@Builder
public class NoticeDetailDto {
    private Long noticeId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private CommonStatus status;
    private PostStatus postStatus;
    private String content;
    private int visitCount;

    public static NoticeDetailDto noticeToDetailDto(Notice notice) {
        return NoticeDetailDto.builder()
                .beginDate(notice.getBeginDate())
                .thumbnailImage(notice.getThumbnailImage())
                .status(notice.getStatus())
                .title(notice.getTitle())
                .noticeId(notice.getId())
                .postStatus(notice.getPostStatus())
                .content(notice.getContent())
                .visitCount(notice.getVisitCount())
                .build();
    }
}
