package me.kjs.mall.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.order.OrderProductRepository;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.review.dto.ReviewCreateDto;
import me.kjs.mall.review.exception.NotAvailableOrderProductReviewException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;
import static me.kjs.mall.common.util.ThrowUtil.notOwnerThrow;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public Review createReview(ReviewCreateDto reviewCreateDto, Long orderProductId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(NoExistIdException::new);
        if (!orderProduct.isAvailableReview()) {
            throw new NotAvailableOrderProductReviewException();
        }
        notOwnerThrow(orderProduct, member);
        Review review = orderProduct.createReview(reviewCreateDto, member);
        return reviewRepository.save(review);
    }

    @Transactional
    public void useReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        review.updateStatus(CommonStatus.USED);
    }

    private Review findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(review);
        return review;
    }

    @Transactional
    public void unUseReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        review.updateStatus(CommonStatus.UN_USED);
    }
}
