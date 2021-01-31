package me.kjs.mall.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.review.Review;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private String content;
    private List<String> images;
    private int score;
    private LocalDateTime reviewDateTime;
    private String reviewerName;
    private CommonStatus status;
    private String productName;
    private Long productId;

    public static ReviewDto reviewToDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .images(review.getImages())
                .score(review.getScore())
                .reviewDateTime(review.getReviewDateTime())
                .reviewerName(review.getReviewerName())
                .status(review.getStatus())
                .productName(review.getProductName())
                .productId(review.getProductId())
                .build();
    }
}
