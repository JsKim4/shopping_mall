package me.kjs.mall.api.partial;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.partial.BestReviewProduct;
import me.kjs.mall.partial.BestReviewService;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.review.Review;
import me.kjs.mall.review.dto.ReviewCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PartialBestReviewApiControllerTest extends BaseTest {

    @Test
    void useBestProduct() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        Long productId = product.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/best/used/{productId}", productId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("create-best-review-product",
                        pathParameters(
                                parameterWithName("productId").description("상품 고유 번호")
                        )
                ));
        List<BestReviewProduct> bestReviewProducts = bestReviewProductRepository.findAll();
        assertTrue(bestReviewProducts.stream().anyMatch(brp -> brp.getProduct() == product));
    }

    @Test
    void useBestProductFailByAlready() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        Long productId = product.getId();

        bestReviewService.createBestReviewProduct(productId);
        ExceptionStatus exceptionStatus = ExceptionStatus.ALREADY_EXIST_BEST_REVIEW_PRODUCT;
        int expectStatus = exceptionStatus.getStatus();
        String expectMessage = exceptionStatus.getMessage();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/best/used/{productId}", productId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(expectStatus))
                .andExpect(jsonPath("message").value(expectMessage));
        List<BestReviewProduct> bestReviewProducts = bestReviewProductRepository.findAll();
        assertTrue(bestReviewProducts.stream().anyMatch(brp -> brp.getProduct() == product));
    }

    @Test
    void unUseBestProduct() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        Long productId = product.getId();

        bestReviewService.createBestReviewProduct(productId);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/best/unused/{productId}", productId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("delete-best-review-product",
                        pathParameters(
                                parameterWithName("productId").description("상품 고유 번호")
                        )
                ));
        List<BestReviewProduct> bestReviewProducts = bestReviewProductRepository.findAll();
        assertFalse(bestReviewProducts.stream().anyMatch(brp -> brp.getProduct() == product));
    }

    @SpyBean
    BestReviewService bestReviewService;

    @Test
    void useBestReview() throws Exception {
        Member member = memberRepository.findByRefreshToken(getTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Review> reviews = new ArrayList<>();
        OrderProduct orderProduct = mock(OrderProduct.class);
        Product product = products.get(0);
        given(orderProduct.getProduct()).willReturn(product);
        bestReviewService.createBestReviewProduct(product.getId());
        for (int i = 0; i < 5; i++) {
            Review review = Review.createReview(ReviewCreateDto.builder()
                    .score(5)
                    .images(Arrays.asList("image1", "image2", "image3"))
                    .content("content-" + i).build(), orderProduct, member);
            reviews.add(reviewRepository.save(review));
        }
        em.flush();
        Review review = reviews.get(0);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/reviews/best/used/{reviewId}", review.getId()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("create-best-review",
                        pathParameters(
                                parameterWithName("reviewId").description("상품 리뷰 고유 번호")
                        )
                ));
        List<BestReviewProduct> reviewProducts = bestReviewProductRepository.findAll();
        BestReviewProduct bestReviewProduct = reviewProducts.stream().filter(brp -> brp.getProduct() == product).findFirst().orElseThrow(NoExistIdException::new);
        assertTrue(bestReviewProduct.getBestReviews().stream().anyMatch(br -> br.getReview() == review));
    }

    @Test
    void unUseBestReview() throws Exception {
        Member member = memberRepository.findByRefreshToken(getTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Review> reviews = new ArrayList<>();
        OrderProduct orderProduct = mock(OrderProduct.class);
        Product product = products.get(0);
        given(orderProduct.getProduct()).willReturn(product);
        bestReviewService.createBestReviewProduct(product.getId());
        for (int i = 0; i < 5; i++) {
            Review review = Review.createReview(ReviewCreateDto.builder()
                    .score(5)
                    .images(Arrays.asList("image1", "image2", "image3"))
                    .content("content-" + i).build(), orderProduct, member);
            reviews.add(reviewRepository.save(review));
        }
        Review review = reviews.get(0);
        em.flush();
        bestReviewService.createBestReview(review.getId());
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/reviews/best/unused/{reviewId}", review.getId()))
                .andDo(document("delete-best-review",
                        pathParameters(
                                parameterWithName("reviewId").description("상품 리뷰 고유 번호")
                        )
                ));

        em.flush();
        List<BestReviewProduct> reviewProducts = bestReviewProductRepository.findAll();
        BestReviewProduct bestReviewProduct = reviewProducts.stream().filter(brp -> brp.getProduct() == product).findFirst().orElseThrow(NoExistIdException::new);
        assertFalse(bestReviewProduct.getBestReviews().stream().anyMatch(br -> br.getReview() == review));
    }

    @Test
    public void queryBestReview() throws Exception {
        Member member = memberRepository.findByRefreshToken(getTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Review> reviews = new ArrayList<>();
        OrderProduct orderProduct = mock(OrderProduct.class);
        Product product = products.get(0);
        given(orderProduct.getProduct()).willReturn(product);
        bestReviewService.createBestReviewProduct(product.getId());
        for (int i = 0; i < 5; i++) {
            Review review = Review.createReview(ReviewCreateDto.builder()
                    .score(5)
                    .images(Arrays.asList("image1", "image2", "image3"))
                    .content("content-" + i).build(), orderProduct, member);
            reviews.add(reviewRepository.save(review));
        }
        em.flush();
        for (Review review : reviews) {
            bestReviewService.createBestReview(review.getId());
        }
        CommonSearchCondition commonSearchCondition = CommonSearchCondition.builder()
                .contents(3)
                .page(0)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/best/reviews")
                .param("contents", String.valueOf(commonSearchCondition.getContents()))
                .param("page", String.valueOf(commonSearchCondition.getPage())))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("query-best_review",
                        requestParameters(
                                parameterWithName("contents").description("페이지 하나에 검색될 개수 (_기본 10)"),
                                parameterWithName("page").description("페이지 번호 (0번부터 시작_기본 0)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("페이지 콘텐츠 개수"),
                                fieldWithPath("data.totalCount").description("전체 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].product.productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].product.name").description("상품 이름"),
                                fieldWithPath("data.contents[*].product.thumbnailImage").description("썸네일 이미지 목록"),
                                fieldWithPath("data.contents[*].product.originPrice").description("원 판매 가격"),
                                fieldWithPath("data.contents[*].product.stock").description("재고량"),
                                fieldWithPath("data.contents[*].product.commonStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[*].product.salesBeginDate").description("판매 시작일"),
                                fieldWithPath("data.contents[*].product.salesEndDate").description("판매 종료일"),
                                fieldWithPath("data.contents[*].product.discountPrice").description("할인 금액"),
                                fieldWithPath("data.contents[*].product.discountPercent").description("할인률"),
                                fieldWithPath("data.contents[*].product.price").description("최종 판매 가격"),
                                fieldWithPath("data.contents[*].reviews[*].reviewId").description("리뷰 고유 번호"),
                                fieldWithPath("data.contents[*].reviews[*].content").description("리뷰 내용"),
                                fieldWithPath("data.contents[*].reviews[*].images").description("리뷰 이미지"),
                                fieldWithPath("data.contents[*].reviews[*].score").description("점수"),
                                fieldWithPath("data.contents[*].reviews[*].reviewDateTime").description("작성 일자").type("Date Time"),
                                fieldWithPath("data.contents[*].reviews[*].reviewerName").description("리뷰 작성자 이름"),
                                fieldWithPath("data.contents[*].reviews[*].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "USED 일떄는 사용중 UNUSED 일때는 관리자 가림 상태")),
                                fieldWithPath("data.contents[*].reviews[*].productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].reviews[*].productName").description("상품명")
                        )
                ));


    }
}