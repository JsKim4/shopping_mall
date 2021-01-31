package me.kjs.mall.story;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.story.dto.StorySearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoryQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<Story> findStoryBySearchCondition(StorySearchCondition storySearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (storySearchCondition.getStatus() != null) {
            booleanBuilder.and(QStory.story.status.eq(storySearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QStory.story.status.notIn(CommonStatus.DELETED));
        }
        if (storySearchCondition.getPostStatus() == PostStatus.PROCESS) {
            booleanBuilder.and(QStory.story.beginDateTime.loe(LocalDateTime.now()));
            booleanBuilder.and(QStory.story.status.eq(CommonStatus.USED));
        } else if (storySearchCondition.getPostStatus() == PostStatus.WAIT) {
            booleanBuilder.and(QStory.story.beginDateTime.gt(LocalDateTime.now()).or(
                    QStory.story.status.eq(CommonStatus.UN_USED).or(
                            QStory.story.status.eq(CommonStatus.CREATED)
                    )
            ));
        }
        QueryResults<Story> results = queryFactory.select(QStory.story)
                .from(QStory.story)
                .where(booleanBuilder)
                .orderBy(QStory.story.beginDateTime.desc())
                .offset(storySearchCondition.getOffset())
                .limit(storySearchCondition.getLimit())
                .fetchResults();
        CommonPage<Story> commonPage = new CommonPage(results, storySearchCondition.getPage());
        return commonPage;
    }
}
