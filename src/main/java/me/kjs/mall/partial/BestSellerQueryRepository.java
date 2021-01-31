package me.kjs.mall.partial;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BestSellerQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<BestSeller> findBestSellerByMaxDateAndSearchCondition(CommonSearchCondition commonSearchCondition) {
        QueryResults<BestSeller> results = queryFactory.select(QBestSeller.bestSeller)
                .from(QBestSeller.bestSeller)
                .where(QBestSeller.bestSeller.date.eq(
                        queryFactory.select(QBestSeller.bestSeller.date.max())
                                .from(QBestSeller.bestSeller)
                ))
                .orderBy(QBestSeller.bestSeller.salesRate.desc())
                .limit(commonSearchCondition.getLimit())
                .offset(commonSearchCondition.getOffset())
                .fetchResults();
        for (BestSeller result : results.getResults()) {
            result.getProduct().loading();
        }
        return new CommonPage(results, commonSearchCondition.getPage());
    }
}
