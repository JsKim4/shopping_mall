package me.kjs.mall.point;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.point.dto.PointSpecificAndAmountDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PointSpecificQueryRepository {
    private final EntityManager em;
    private final PointSpecificRepository pointSpecificRepository;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public List<PointSpecific> findAllByUsablePointSpecifics(Member member) {
        return queryFactory.selectFrom(QPointSpecific.pointSpecific)
                .where(QPointSpecific.pointSpecific.id.in(
                        queryFactory.select(QPointSpecific.pointSpecific.accumSpecific.id)
                                .from(QPointSpecific.pointSpecific)
                                .where(QPointSpecific.pointSpecific.member.eq(member))
                                .groupBy(QPointSpecific.pointSpecific.accumSpecific.id)
                                .having(QPointSpecific.pointSpecific.amount.sum().gt(0))
                                .fetch()
                )).fetch();
    }

    public int findUsablePoint(Member member) {
        List<Integer> fetch = queryFactory.select(QPointSpecific.pointSpecific.amount.sum())
                .from(QPointSpecific.pointSpecific)
                .where(QPointSpecific.pointSpecific.member.eq(member))
                .groupBy(QPointSpecific.pointSpecific.accumSpecific.id)
                .having(QPointSpecific.pointSpecific.amount.sum().gt(0))
                .fetch();
        int sum = 0;
        for (Integer integer : fetch) {
            sum += integer;
        }

        return sum;
    }

    public List<PointSpecificAndAmountDto> findExpiredPoint() {
        List<PointSpecificIdAndAmountDto> fetch = queryFactory.select(Projections.constructor(PointSpecificIdAndAmountDto.class,
                QPointSpecific.pointSpecific.accumSpecific.id,
                QPointSpecific.pointSpecific.amount.sum()))
                .from(QPointSpecific.pointSpecific)
                .groupBy(QPointSpecific.pointSpecific.accumSpecific)
                .having(QPointSpecific.pointSpecific.amount.sum().gt(0).and(
                        QPointSpecific.pointSpecific.remainPointExpireDate.min().loe(LocalDate.now())
                )).fetch();


        List<PointSpecificAndAmountDto> result = new ArrayList<>();
        for (PointSpecificIdAndAmountDto pointSpecificIdAndAmountDto : fetch) {
            PointSpecific pointSpecific = pointSpecificRepository.findById(pointSpecificIdAndAmountDto.getId()).orElseThrow(NoExistIdException::new);
            result.add(new PointSpecificAndAmountDto(pointSpecific, pointSpecificIdAndAmountDto.getAmount()));
        }
        return result;

    }
}
