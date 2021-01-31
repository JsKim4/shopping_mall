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
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BestReviewProductQueryRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<BestReviewProduct> findBestReviewProductByCommonSearchCondition(CommonSearchCondition commonSearchCondition) {
        QueryResults<BestReviewProduct> bestReviewProductQueryResults = queryFactory.selectFrom(QBestReviewProduct.bestReviewProduct)
                .offset(commonSearchCondition.getOffset())
                .limit(commonSearchCondition.getLimit())
                .fetchResults();
        for (BestReviewProduct result : bestReviewProductQueryResults.getResults()) {
            result.loading();
        }
        return new CommonPage(bestReviewProductQueryResults, commonSearchCondition.getPage());
    }
}
