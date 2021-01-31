package me.kjs.mall.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.partial.exception.AlreadyExistBestReviewProductException;
import me.kjs.mall.partial.exception.NotRegisterBestReviewProductException;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.review.Review;
import me.kjs.mall.review.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BestReviewService {

    private final ReviewRepository reviewRepository;
    private final BestReviewRepository bestReviewRepository;
    private final BestReviewProductRepository bestReviewProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createBestReviewProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoExistIdException::new);
        boolean exist = bestReviewProductRepository.existsByProduct(product);
        if (exist) {
            throw new AlreadyExistBestReviewProductException();
        }
        int orders = bestReviewProductRepository.findMaxOrders().orElse(0) + 1;
        BestReviewProduct bestReviewProduct = BestReviewProduct.createBestReviewProduct(product, orders);
        bestReviewProduct.loading();
        bestReviewProductRepository.save(bestReviewProduct);
    }

    @Transactional
    public void deleteBestReviewProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoExistIdException::new);
        BestReviewProduct bestReviewProduct = bestReviewProductRepository.findByProduct(product).orElseThrow(NoExistIdException::new);
        bestReviewProduct.delete();
        bestReviewProductRepository.delete(bestReviewProduct);
    }

    @Transactional
    public void createBestReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoExistIdException::new);
        BestReviewProduct bestReviewProduct = bestReviewProductRepository.findByProduct(review.getProduct()).orElseThrow(NotRegisterBestReviewProductException::new);
        BestReview bestReview = bestReviewRepository.findTopByReview(review).orElse(BestReview.createBestReview(review, bestReviewProduct));
        bestReviewProduct.addBestReview(bestReview);
    }

    @Transactional
    public void deleteBestReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoExistIdException::new);
        BestReview bestReview = bestReviewRepository.findTopByReview(review).orElseThrow(NoExistIdException::new);
        bestReview.delete();
        bestReviewRepository.delete(bestReview);
    }
}
