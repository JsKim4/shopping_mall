package me.kjs.mall.point;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.point.dto.PointSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PointQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonSlice<Point> findPointByMemberAndSearchCondition(Member member, PointSearchCondition pointSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QPoint.point.member.eq(member));
        if (pointSearchCondition.getPointState() == PointState.ACCUMULATE) {
            booleanBuilder.and(QPoint.point.pointState.eq(PointState.ACCUMULATE));
        } else if (pointSearchCondition.getPointState() == PointState.USE) {
            booleanBuilder.and(QPoint.point.pointState.eq(PointState.USE));
        }

        List<Point> pointList = queryFactory.select(QPoint.point)
                .from(QPoint.point)
                .where(booleanBuilder)
                .limit(pointSearchCondition.getSliceLimit())
                .offset(pointSearchCondition.getOffset())
                .fetch();
        CommonSlice commonSlice = new CommonSlice(pointList, pointSearchCondition);
        return commonSlice;
    }

    public Optional<Point> findByOrderSpecific(OrderSpecific orderSpecific) {
        return Optional.ofNullable(
                queryFactory.select(QPoint.point)
                        .from(QPoint.point)
                        .where(QPoint.point.order.eq(orderSpecific.getOrder())
                                .and(QPoint.point.pointState.eq(PointState.USE)))
                        .fetchFirst()
        );
    }
}

