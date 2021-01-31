package me.kjs.mall.notice.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.notice.Notice;

import java.time.LocalDate;

@Getter
@Builder
public class NoticeSimpleDto {
    private Long noticeId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private CommonStatus status;
    private PostStatus postStatus;
    private int visitCount;


    public static NoticeSimpleDto noticeToSimpleDto(Notice notice) {
        return NoticeSimpleDto.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .thumbnailImage(notice.getThumbnailImage())
                .beginDate(notice.getBeginDate())
                .status(notice.getStatus())
                .postStatus(notice.getPostStatus())
                .visitCount(notice.getVisitCount())
                .build();


    }
}
