package me.kjs.mall.api.story;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.story.Story;
import me.kjs.mall.story.StoryQueryRepository;
import me.kjs.mall.story.StoryService;
import me.kjs.mall.story.dto.StoryDetailDto;
import me.kjs.mall.story.dto.StorySaveDto;
import me.kjs.mall.story.dto.StorySearchCondition;
import me.kjs.mall.story.dto.StorySimpleDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stories")
@Slf4j
public class StoryApiController {


    private final StoryService storyService;
    private final StoryQueryRepository storyQueryRepository;

    @GetMapping
    public ResponseDto queryStories(@Validated StorySearchCondition storySearchCondition,
                                    Errors errors,
                                    @CurrentMember Member member) {
        hasErrorsThrow(errors);
        if (storySearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "delete story don't query");
        }
        if (!hasRole(member, AccountRole.STORY)) {
            if (storySearchCondition.getStatus() == CommonStatus.UN_USED ||
                    storySearchCondition.getStatus() == CommonStatus.CREATED) {
                errors.rejectValue("status", "wrong value", "not admin don't query un_used or created");
            }
            if (storySearchCondition.getPostStatus() != PostStatus.PROCESS) {
                errors.rejectValue("postStatus", "wrong value", "user query postStatus only PROCESS");
            }
        }
        hasErrorsThrow(errors);

        CommonPage<Story> stories = storyQueryRepository.findStoryBySearchCondition(storySearchCondition);

        CommonPage body = stories.updateContent(stories.getContents().stream().map(StorySimpleDto::storyToSimpleDto).collect(Collectors.toList()));

        return ResponseDto.ok(body);
    }


    /**
     * 멱등성 보장 안됨
     */
    @GetMapping("/{storyId}")
    public ResponseDto queryStory(@PathVariable("storyId") Long storyId,
                                  @CurrentMember Member member) {
        Story story = storyService.findStoryById(storyId);
        if (!hasRole(member, AccountRole.STORY)) {
            if (!story.isUsed()) {
                throw new NoExistIdException();
            }
        }
        storyService.visit(story.getId());
        StoryDetailDto body = StoryDetailDto.storyToDetailDto(story);
        return ResponseDto.ok(body);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto createStory(@RequestBody @Validated StorySaveDto storySaveDto,
                                   Errors errors) {

        hasErrorsThrow(errors);

        Story story = storyService.createStory(storySaveDto);

        StoryDetailDto body = StoryDetailDto.storyToDetailDto(story);

        return ResponseDto.created(body);
    }


    @PutMapping("/{storyId}")
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto updateStory(@PathVariable("storyId") Long storyId,
                                   @RequestBody @Validated StorySaveDto storySaveDto,
                                   Errors errors) {
        hasErrorsThrow(errors);

        storyService.updateStory(storySaveDto, storyId);

        return ResponseDto.noContent();
    }

    @PutMapping("/used/{storyId}")
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto useStory(@PathVariable("storyId") Long storyId) {
        storyService.useStory(storyId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{storyId}")
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto unUseStory(@PathVariable("storyId") Long storyId) {
        storyService.unUseStory(storyId);
        return ResponseDto.noContent();
    }


    @DeleteMapping("/{storyId}")
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto deleteStory(@PathVariable("storyId") Long storyId) {
        storyService.deleteStory(storyId);
        return ResponseDto.noContent();
    }
}
