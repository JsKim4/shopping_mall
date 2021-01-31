package me.kjs.mall.api.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.review.Review;
import me.kjs.mall.review.ReviewQueryRepository;
import me.kjs.mall.review.ReviewService;
import me.kjs.mall.review.dto.ReviewCreateDto;
import me.kjs.mall.review.dto.ReviewDto;
import me.kjs.mall.review.dto.ReviewSearchCondition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewApiController {

    private final ReviewService reviewService;

    private final ReviewQueryRepository reviewQueryRepository;

    @PostMapping("/order/product/{orderProductId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto createOrderProductReview(@PathVariable("orderProductId") Long orderProductId,
                                                @RequestBody @Validated ReviewCreateDto reviewCreateDto,
                                                @CurrentMember Member member,
                                                Errors errors) {
        Review review = reviewService.createReview(reviewCreateDto, orderProductId, member.getId());
        ReviewDto reviewDto = ReviewDto.reviewToDto(review);

        return ResponseDto.created(reviewDto);
    }

    @GetMapping
    public ResponseDto queryReviewByProductAndReviewSearchCondition(@Validated ReviewSearchCondition reviewSearchCondition,
                                                                    @CurrentMember Member member,
                                                                    Errors errors) {
        hasErrorsThrow(errors);
        if (reviewSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "status can;t DELETED");
        }
        if (!hasRole(member, AccountRole.REVIEW)) {
            if (reviewSearchCondition.getProductId() == null) {
                errors.rejectValue("productId", "wrong value", "productId can't empty");
            }
            if (reviewSearchCondition.getStatus() != CommonStatus.USED) {
                errors.rejectValue("status", "wrong value", "status will be USED only");
            }
        }
        hasErrorsThrow(errors);
        CommonSlice<Review> commonSlice = reviewQueryRepository.findReviewByProductIdAndSearchCondition(reviewSearchCondition);

        CommonSlice body = commonSlice.updateContent(
                commonSlice.getContents().stream().map(ReviewDto::reviewToDto).collect(Collectors.toList())
        );
        return ResponseDto.ok(body);
    }

    @PutMapping("/unused/{reviewId}")
    public ResponseDto unUseReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.unUseReview(reviewId);
        return ResponseDto.noContent();
    }

    @PutMapping("/used/{reviewId}")
    public ResponseDto useReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.useReview(reviewId);
        return ResponseDto.noContent();
    }


}
