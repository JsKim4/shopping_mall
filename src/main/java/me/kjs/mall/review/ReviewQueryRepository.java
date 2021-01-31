package me.kjs.mall.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.review.dto.ReviewOrderType;
import me.kjs.mall.review.dto.ReviewSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReviewQueryRepository {
    private final EntityManager em;

    private final ProductRepository productRepository;

    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void setUp() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    public CommonSlice<Review> findReviewByProductIdAndSearchCondition(ReviewSearchCondition reviewSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (reviewSearchCondition.getProductId() != null) {
            Product product = productRepository.findById(reviewSearchCondition.getProductId()).orElseThrow(NoExistIdException::new);
            booleanBuilder.and(QReview.review.product.eq(product));
        }

        if (reviewSearchCondition.getStatus() != null) {
            booleanBuilder.and(QReview.review.status.eq(reviewSearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QReview.review.status.notIn(CommonStatus.DELETED));
        }

        JPAQuery<Review> reviewForm = jpaQueryFactory.select(QReview.review)
                .from(QReview.review)
                .where(booleanBuilder);

        if (reviewSearchCondition.getReviewOrderType() == ReviewOrderType.SCORE_DESC) {
            reviewForm = reviewForm.orderBy(QReview.review.score.desc());
        } else if (reviewSearchCondition.getReviewOrderType() == ReviewOrderType.CREATED) {
            reviewForm = reviewForm.orderBy(QReview.review.reviewDateTime.desc());
        }
        List<Review> reviews = reviewForm
                .offset(reviewSearchCondition.getOffset())
                .limit(reviewSearchCondition.getSliceLimit())
                .fetch();
        for (Review review : reviews) {
            review.loading();
        }

        CommonSlice<Review> commonSlice = new CommonSlice(reviews, reviewSearchCondition);
        return commonSlice;
    }
}
