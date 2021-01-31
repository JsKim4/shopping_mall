package me.kjs.mall.api.wish;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WishApiControllerTest extends BaseTest {


    @Test
    @DisplayName("/api/products/wish 찜 생성 성공 케이스")
    void createProductsWish() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        TokenDto tokenDto = getTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        Product product = products.get(0);
        boolean isNotExist = wishQueryRepository.existByMemberIdAndProductId(member.getId(), product.getId());

        assertFalse(isNotExist);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/products/wish/{productId}", product.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.productId").value(product.getId()))
                .andExpect(jsonPath("data.name").value(product.getBaseProductName()))
                .andExpect(jsonPath("data.discountPercent").value(product.getDiscountPercent()))
                .andExpect(jsonPath("data.originPrice").value(product.getOriginPrice()))
                .andExpect(jsonPath("data.price").value(product.getPrice()))
                .andExpect(jsonPath("data.thumbnail").isArray())
                .andDo(document(
                        "create-products-wish",
                        pathParameters(
                                parameterWithName("productId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.productId").description("판매 상품 고유 번호"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.discountPercent").description("할인 퍼센트"),
                                fieldWithPath("data.originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.thumbnail").description("상품 썸네일 이미지")
                        )
                ));


        boolean isExist = wishQueryRepository.existByMemberIdAndProductId(member.getId(), product.getId());

        assertTrue(isExist);
    }

    @Test
    @DisplayName("/api/products/wish 찜 삭제 성공")
    void deleteProductsWish() throws Exception {

        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        TokenDto tokenDto = getTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        Product product = products.get(0);

        wishService.createWish(member.getId(), product.getId());

        boolean isExist = wishQueryRepository.existByMemberIdAndProductId(member.getId(), product.getId());

        assertTrue(isExist);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/products/wish/{productId}", product.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "delete-products-wish",
                        pathParameters(
                                parameterWithName("productId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 204"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null").optional()
                        )
                ));


        boolean isNotExist = wishQueryRepository.existByMemberIdAndProductId(member.getId(), product.getId());

        assertFalse(isNotExist);
    }

    @Test
    @DisplayName("/api/products/wish 찜 리스트 조회 성공")
    void queryProductsWish() throws Exception {

        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        TokenDto tokenDto = getTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        for (int i = 0; i < 10 && i < products.size(); i++) {
            Product product = products.get(0);

            wishService.createWish(member.getId(), product.getId());
        }


        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/wish")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data[0].productId").exists())
                .andExpect(jsonPath("data[0].name").exists())
                .andExpect(jsonPath("data[0].discountPercent").exists())
                .andExpect(jsonPath("data[0].originPrice").exists())
                .andExpect(jsonPath("data[0].price").exists())
                .andExpect(jsonPath("data[0].thumbnail").isArray())
                .andDo(document(
                        "query-products-wish",
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[].productId").description("판매 상품 고유 번호"),
                                fieldWithPath("data[].name").description("상품 이름"),
                                fieldWithPath("data[].discountPercent").description("할인 퍼센트"),
                                fieldWithPath("data[].originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data[].price").description("상품 가격"),
                                fieldWithPath("data[].thumbnail").description("상품 썸네일 이미지")
                        )
                ));
    }


}