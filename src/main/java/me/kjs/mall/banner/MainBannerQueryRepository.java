package me.kjs.mall.banner;

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
public class MainBannerQueryRepository {

    private final EntityManager entityManager;

    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void setUp() {
        jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    public CommonPage<MainBanner> findMainBannerBySearchCondition(CommonSearchCondition commonSearchCondition) {
        QueryResults<MainBanner> result = jpaQueryFactory.selectFrom(QMainBanner.mainBanner)
                .offset(commonSearchCondition.getOffset())
                .limit(commonSearchCondition.getLimit())
                .fetchResults();
        return new CommonPage<>(result, commonSearchCondition.getPage());
    }
}
