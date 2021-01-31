package me.kjs.mall.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.event.dto.EventSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventQueryRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }


    public CommonPage<Event> findEventBySearchCondition(EventSearchCondition eventSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (eventSearchCondition.getStatus() != null) {
            booleanBuilder.and(QEvent.event.status.eq(eventSearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QEvent.event.status.notIn(CommonStatus.DELETED));
        }
        if (eventSearchCondition.getEventStatus() == EventStatus.PROCESS) {
            booleanBuilder.and(QEvent.event.beginDateTime.loe(LocalDateTime.now()));
            booleanBuilder.and(QEvent.event.endDateTime.goe(LocalDateTime.now()));
        } else if (eventSearchCondition.getEventStatus() == EventStatus.END) {
            booleanBuilder.and(QEvent.event.endDateTime.lt(LocalDateTime.now()));
        } else if (eventSearchCondition.getEventStatus() == EventStatus.WAIT) {
            booleanBuilder.and(QEvent.event.beginDateTime.gt(LocalDateTime.now()));
        }
        QueryResults<Event> results = queryFactory.select(QEvent.event)
                .from(QEvent.event)
                .where(booleanBuilder)
                .orderBy(QEvent.event.beginDateTime.desc())
                .offset(eventSearchCondition.getOffset())
                .limit(eventSearchCondition.getLimit())
                .fetchResults();
        for (Event result : results.getResults()) {
            result.loading();
        }
        CommonPage<Event> commonPage = new CommonPage(results, eventSearchCondition.getPage());
        return commonPage;
    }
}
