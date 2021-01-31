package me.kjs.mall.api.review;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.review.Review;
import me.kjs.mall.review.dto.ReviewCreateDto;
import me.kjs.mall.review.dto.ReviewOrderType;
import me.kjs.mall.review.dto.ReviewSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
class ReviewApiControllerTest extends BaseTest {

    @Test
    void reviewCreateTest() throws Exception {
        TokenDto userTokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(generateOrderCreateDto(), member.getId());
        em.flush();
        Long orderProductId = 0L;
        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                orderProduct.modifyOrderState(OrderState.ORDER_ACCEPT);
                orderProductId = orderProduct.getId();
                break;
            }
            break;
        }

        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content("content")
                .images(Arrays.asList("image0001", "image0002", "image0003"))
                .score(5)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/reviews/order/product/{orderProduct}", orderProductId)
                .header("X-AUTH-TOKEN", userTokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.reviewId").exists())
                .andExpect(jsonPath("data.content").value(reviewCreateDto.getContent()))
                .andExpect(jsonPath("data.images").isArray())
                .andExpect(jsonPath("data.score").value(reviewCreateDto.getScore()))
                .andExpect(jsonPath("data.reviewDateTime").exists())
                .andExpect(jsonPath("data.reviewerName").value(member.getMaskingName()))
                .andExpect(jsonPath("data.status").value(CommonStatus.USED.name()))
                .andDo(document("create-review",
                        requestFields(
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("images").description("첨부 이미지 리스트").optional(),
                                fieldWithPath("score").description("점수 1 ~ 5")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.reviewId").description("리뷰 고유 번호"),
                                fieldWithPath("data.content").description("리뷰 내용"),
                                fieldWithPath("data.images").description("리뷰 이미지"),
                                fieldWithPath("data.score").description("점수"),
                                fieldWithPath("data.reviewDateTime").description("작성 일자").type("Date Time"),
                                fieldWithPath("data.reviewerName").description("리뷰 작성자 이름"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "USED 일떄는 사용중 UNUSED 일때는 관리자 가림 상태")),
                                fieldWithPath("data.productId").description("상품 고유 번호"),
                                fieldWithPath("data.productName").description("상품명")
                        )));
    }

    @Test
    void reviewQueryTest() throws Exception {
        TokenDto userTokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Long productId = 0L;
        for (int i = 0; i < 6; i++) {
            ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                    .content("content " + i)
                    .images(Arrays.asList("image0001", "image0002", "image0003"))
                    .score(i % 5 + 1)
                    .build();
            Order order = orderService.createOrder(generateOrderCreateDto(), member.getId());
            for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
                for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                    orderProduct.modifyOrderState(OrderState.ORDER_ACCEPT);
                    reviewService.createReview(reviewCreateDto, orderProduct.getId(), member.getId());
                    productId = orderProduct.getProduct().getId();
                }
            }
        }
        ReviewSearchCondition reviewSearchCondition = ReviewSearchCondition.builder()
                .contents(5)
                .page(0)
                .productId(productId)
                .status(CommonStatus.USED)
                .reviewOrderType(ReviewOrderType.SCORE_DESC)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/reviews")
                .param("contents", String.valueOf(reviewSearchCondition.getContents()))
                .param("page", String.valueOf(reviewSearchCondition.getPage()))
                .param("productId", String.valueOf(reviewSearchCondition.getProductId()))
                .param("status", String.valueOf(reviewSearchCondition.getStatus()))
                .param("reviewOrderType", String.valueOf(reviewSearchCondition.getReviewOrderType())))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.hasNext").value(YnType.Y.name()))
                .andExpect(jsonPath("data.contentCount").value(reviewSearchCondition.getContents()))
                .andExpect(jsonPath("data.nowPage").value(reviewSearchCondition.getPage()))
                .andExpect(jsonPath("data.contents[*].reviewId").exists())
                .andExpect(jsonPath("data.contents[*].content").exists())
                .andExpect(jsonPath("data.contents[*].images").exists())
                .andExpect(jsonPath("data.contents[*].score").exists())
                .andExpect(jsonPath("data.contents[*].reviewDateTime").exists())
                .andExpect(jsonPath("data.contents[*].reviewerName").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andDo(document("query-review-product",
                        requestParameters(
                                parameterWithName("contents").description("요청할 개수(최소 5 최대 30)"),
                                parameterWithName("productId").description("상품 고유번호 / 일반유저 일경우 필수"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "일반 유저일 경우 USED ONLY")),
                                parameterWithName("reviewOrderType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.REVIEW_ORDER_TYPE)),
                                parameterWithName("page").description("요청할 페이지 (0 부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("요청한 콘텐츠 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.hasNext").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다음 페이지 존재 여부")),
                                fieldWithPath("data.contents[*].reviewId").description("리뷰 고유 번호"),
                                fieldWithPath("data.contents[*].content").description("리뷰 내용"),
                                fieldWithPath("data.contents[*].images").description("리뷰 이미지 리스트"),
                                fieldWithPath("data.contents[*].score").description("점수"),
                                fieldWithPath("data.contents[*].reviewDateTime").description("작성 일자").type("Date Time"),
                                fieldWithPath("data.contents[*].reviewerName").description("리뷰 작성자 이름"),
                                fieldWithPath("data.contents[*].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "USED 일떄는 사용중 UNUSED 일때는 관리자 가림 상태")),
                                fieldWithPath("data.contents[*].productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].productName").description("상품명")
                        )

                ));
    }


    @Test
    void reviewUseTest() throws Exception {
        TokenDto userTokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content("content")
                .images(Arrays.asList("image0001", "image0002", "image0003"))
                .score(3)
                .build();
        Order order = orderService.createOrder(generateOrderCreateDto(), member.getId());
        em.flush();
        Long reviewId = null;
        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                orderProduct.modifyOrderState(OrderState.ORDER_ACCEPT);
                Review review = reviewService.createReview(reviewCreateDto, orderProduct.getId(), member.getId());
                reviewId = review.getId();
            }
        }

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/reviews/used/{reviewId}", reviewId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-review-used",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 고유 번호")
                        )
                ));
    }


    @Test
    void reviewUnUseTest() throws Exception {
        TokenDto userTokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .content("content")
                .images(Arrays.asList("image0001", "image0002", "image0003"))
                .score(3)
                .build();
        Order order = orderService.createOrder(generateOrderCreateDto(), member.getId());
        em.flush();
        Long reviewId = null;
        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                orderProduct.modifyOrderState(OrderState.ORDER_ACCEPT);
                Review review = reviewService.createReview(reviewCreateDto, orderProduct.getId(), member.getId());
                reviewId = review.getId();
            }
        }

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/reviews/unused/{reviewId}", reviewId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-review-unused",
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 고유 번호")
                        )
                ));

    }


    private OrderCreateDto generateOrderCreateDto() {
        List<Product> allProducts = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Product> products = AvailableUtil.isUsedFilter(allProducts);
        List<Product> result = products.stream().collect(Collectors.toList());
        List<ProductIdAndQuantityDto> productIdAndQuantityDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = result.get(i);
            ProductIdAndQuantityDto productIdAndQuantityDto = ProductIdAndQuantityDto.builder()
                    .productId(product.getId())
                    .quantity(i + 1)
                    .build();
            productIdAndQuantityDtos.add(productIdAndQuantityDto);
        }

        return OrderCreateDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient1")
                                .addressSimple("address1")
                                .addressDetail("addressDetail1")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build()
                ))
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();
    }

}