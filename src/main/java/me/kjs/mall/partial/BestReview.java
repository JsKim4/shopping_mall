package me.kjs.mall.partial;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.review.Review;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BestReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "best_review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "best_review_product_id")
    private BestReviewProduct bestReviewProduct;

    public static BestReview createBestReview(Review review, BestReviewProduct bestReviewProduct) {
        BestReview bestReview = BestReview.builder()
                .bestReviewProduct(bestReviewProduct)
                .review(review)
                .build();
        return bestReview;
    }

    public void delete() {
        bestReviewProduct.deleteBestReview(this);
        bestReviewProduct = null;
    }

    public void loading() {
        review.loading();
    }

    public Review getReview() {
        return review;
    }
}