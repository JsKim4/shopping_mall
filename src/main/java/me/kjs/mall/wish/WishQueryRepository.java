package me.kjs.mall.wish;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WishQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public boolean existByMemberIdAndProductId(Long memberId, Long productId) {
        Integer fetchFirst = queryFactory.selectOne()
                .from(QWish.wish)
                .where(QWish.wish.product.id.eq(productId).and(
                        QWish.wish.member.id.eq(memberId))).fetchFirst();
        return fetchFirst != null;
    }

    public Wish findWishByMemberIdAndProductId(Long memberId, Long productId) {
        return queryFactory.selectFrom(QWish.wish)
                .where(QWish.wish.product.id.eq(productId).and(
                        QWish.wish.member.id.eq(memberId))).fetchFirst();
    }

}
