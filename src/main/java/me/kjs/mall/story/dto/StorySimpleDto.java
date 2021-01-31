package me.kjs.mall.story.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.story.Story;

import java.time.LocalDate;


@Getter
@Builder
public class StorySimpleDto {
    private Long storyId;
    private String title;
    private String thumbnailImage;
    private LocalDate beginDate;
    private CommonStatus status;
    private PostStatus postStatus;
    private String contents;
    private int visitCount;


    public static StorySimpleDto storyToSimpleDto(Story story) {
        return StorySimpleDto.builder()
                .storyId(story.getId())
                .title(story.getTitle())
                .thumbnailImage(story.getThumbnailImage())
                .beginDate(story.getBeginDate())
                .status(story.getStatus())
                .postStatus(story.getPostStatus())
                .contents(story.getContent())
                .visitCount(story.getVisitCount())
                .build();


    }
}
