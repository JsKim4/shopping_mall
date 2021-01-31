package me.kjs.mall.notice;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.notice.dto.NoticeSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<Notice> findNoticeBySearchCondition(NoticeSearchCondition noticeSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (noticeSearchCondition.getStatus() != null) {
            booleanBuilder.and(QNotice.notice.status.eq(noticeSearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QNotice.notice.status.notIn(CommonStatus.DELETED));
        }
        if (noticeSearchCondition.getPostStatus() == PostStatus.PROCESS) {
            booleanBuilder.and(QNotice.notice.beginDateTime.loe(LocalDateTime.now()));
            booleanBuilder.and(QNotice.notice.status.eq(CommonStatus.USED));
        } else if (noticeSearchCondition.getPostStatus() == PostStatus.WAIT) {
            booleanBuilder.and(QNotice.notice.beginDateTime.gt(LocalDateTime.now()).or(
                    QNotice.notice.status.eq(CommonStatus.UN_USED).or(
                            QNotice.notice.status.eq(CommonStatus.CREATED)
                    )
            ));
        }
        QueryResults<Notice> results = queryFactory.select(QNotice.notice)
                .from(QNotice.notice)
                .where(booleanBuilder)
                .orderBy(QNotice.notice.beginDateTime.desc())
                .offset(noticeSearchCondition.getOffset())
                .limit(noticeSearchCondition.getLimit())
                .fetchResults();
        CommonPage<Notice> commonPage = new CommonPage(results, noticeSearchCondition.getPage());
        return commonPage;
    }
}
