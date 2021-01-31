package me.kjs.mall.partial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.partial.BestReviewProduct;
import me.kjs.mall.product.dto.ProductSimpleDto;
import me.kjs.mall.review.dto.ReviewDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestReviewProductDto {
    private ProductSimpleDto product;
    private List<ReviewDto> reviews;

    public static BestReviewProductDto BestReviewProductToDto(BestReviewProduct bestReviewProduct) {
        return BestReviewProductDto.builder()
                .product(ProductSimpleDto.productToSimpleDto(bestReviewProduct.getProduct()))
                .reviews(bestReviewProduct.getReviews().stream().map(ReviewDto::reviewToDto).collect(Collectors.toList()))
                .build();
    }
}
