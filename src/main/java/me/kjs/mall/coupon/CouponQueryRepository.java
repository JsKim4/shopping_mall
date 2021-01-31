package me.kjs.mall.coupon;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.coupon.dto.CouponSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CouponQueryRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<Coupon> findCouponBySearchCondition(CouponSearchCondition couponSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (couponSearchCondition.getStatus() != null) {
            booleanBuilder.and(QCoupon.coupon.status.eq(couponSearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QCoupon.coupon.status.notIn(CommonStatus.DELETED));
        }

        QueryResults<Coupon> couponQueryResults = queryFactory.select(QCoupon.coupon)
                .from(QCoupon.coupon)
                .where(booleanBuilder)
                .orderBy(QCoupon.coupon.createdDate.desc())
                .offset(couponSearchCondition.getOffset())
                .limit(couponSearchCondition.getContents())
                .fetchResults();
        CommonPage<Coupon> page = new CommonPage(couponQueryResults, couponSearchCondition.getPage());
        for (Coupon coupon : page.getContents()) {
            coupon.loading();
        }
        return page;
    }
}
