package me.kjs.mall.story.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.story.Story;

import java.time.LocalDate;

@Getter
@Builder
public class StoryDetailDto {
    private Long storyId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private CommonStatus status;
    private PostStatus postStatus;
    private String contents;
    private int visitCount;

    public static StoryDetailDto storyToDetailDto(Story story) {
        return StoryDetailDto.builder()
                .beginDate(story.getBeginDate())
                .thumbnailImage(story.getThumbnailImage())
                .status(story.getStatus())
                .title(story.getTitle())
                .storyId(story.getId())
                .postStatus(story.getPostStatus())
                .contents(story.getContent())
                .visitCount(story.getVisitCount())
                .build();
    }
}
