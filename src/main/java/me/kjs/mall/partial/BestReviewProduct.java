package me.kjs.mall.partial;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.partial.exception.BestReviewSizeOverException;
import me.kjs.mall.product.Product;
import me.kjs.mall.review.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BestReviewProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "best_review_product_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int orders;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bestReviewProduct")
    @Builder.Default
    private List<BestReview> bestReviews = new ArrayList<>();

    public static BestReviewProduct createBestReviewProduct(Product product, int orders) {
        return BestReviewProduct.builder()
                .product(product)
                .orders(orders)
                .build();
    }


    public void delete() {
        for (BestReview bestReview : bestReviews) {
            bestReview.delete();
        }
        bestReviews = new ArrayList<>();
    }

    public void loading() {
        product.loading();
        for (BestReview bestReview : bestReviews) {
            bestReview.loading();
        }
    }

    public Product getProduct() {
        return product;
    }

    public List<BestReview> getBestReviews() {
        return bestReviews;
    }

    public List<Review> getReviews() {
        return bestReviews.stream().map(BestReview::getReview).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void addBestReview(BestReview bestReview) {
        if (bestReviews.size() > 10) {
            throw new BestReviewSizeOverException();
        }
        for (BestReview review : bestReviews) {
            if (review == bestReview) {
                return;
            }
        }
        bestReviews.add(bestReview);
    }

    public void deleteBestReview(BestReview bestReview) {
        bestReviews.remove(bestReview);
    }
}
