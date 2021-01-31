package me.kjs.mall.story;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.story.dto.StorySaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;

    @Transactional
    public Story createStory(StorySaveDto storySaveDto) {
        Story story = Story.createStory(storySaveDto);
        return storyRepository.save(story);
    }

    @Transactional
    public void updateStory(StorySaveDto storySaveDto, Long storyId) {
        Story story = findStoryById(storyId);
        story.updateStory(storySaveDto);
    }


    @Transactional
    public void useStory(Long storyId) {
        Story story = findStoryById(storyId);
        story.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseStory(Long storyId) {
        Story story = findStoryById(storyId);
        story.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void deleteStory(Long storyId) {
        Story story = findStoryById(storyId);
        story.delete();
    }

    public Story findStoryById(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notAvailableThrow(story);
        return story;
    }

    @Transactional
    public void visit(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(NoExistIdException::new);
        story.visit();
    }
}
