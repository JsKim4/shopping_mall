package me.kjs.mall.partial;

import me.kjs.mall.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BestReviewRepository extends JpaRepository<BestReview, Long> {
    Optional<BestReview> findTopByReview(Review review);
}
