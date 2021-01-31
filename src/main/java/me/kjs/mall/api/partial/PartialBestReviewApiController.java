package me.kjs.mall.api.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.partial.BestReviewProduct;
import me.kjs.mall.partial.BestReviewProductQueryRepository;
import me.kjs.mall.partial.BestReviewService;
import me.kjs.mall.partial.dto.BestReviewProductDto;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class PartialBestReviewApiController {

    private final BestReviewService bestReviewService;

    private final BestReviewProductQueryRepository bestReviewProductQueryRepository;

    @GetMapping("/best/reviews")
    public ResponseDto queryBestReview(@Validated CommonSearchCondition commonSearchCondition,
                                       Errors errors) {
        hasErrorsThrow(errors);
        CommonPage<BestReviewProduct> result = bestReviewProductQueryRepository.findBestReviewProductByCommonSearchCondition(commonSearchCondition);
        CommonPage body = result.updateContent(result.getContents().stream().map(BestReviewProductDto::BestReviewProductToDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }

    @PutMapping("/products/best/used/{productId}")
    public ResponseDto createBestReviewProduct(@PathVariable("productId") Long productId) {
        bestReviewService.createBestReviewProduct(productId);
        return ResponseDto.noContent();
    }

    @PutMapping("/products/best/unused/{productId}")
    public ResponseDto deleteBestReviewProduct(@PathVariable("productId") Long productId) {
        bestReviewService.deleteBestReviewProduct(productId);
        return ResponseDto.noContent();
    }

    @PutMapping("/reviews/best/used/{reviewId}")
    public ResponseDto createBestReview(@PathVariable("reviewId") Long reviewId) {
        bestReviewService.createBestReview(reviewId);
        return ResponseDto.noContent();
    }

    @PutMapping("/reviews/best/unused/{reviewId}")
    public ResponseDto deleteBestReview(@PathVariable("reviewId") Long reviewId) {
        bestReviewService.deleteBestReview(reviewId);
        return ResponseDto.noContent();
    }
}
