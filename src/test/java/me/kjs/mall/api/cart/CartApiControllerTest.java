package me.kjs.mall.api.cart;

import me.kjs.mall.cart.Cart;
import me.kjs.mall.cart.CartQuantityModifyDto;
import me.kjs.mall.cart.dto.CartCreateDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/carts 장바구니 생성 성공 케이스")
    void createCartSuccessTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .quantity(10)
                .productId(product.getId())
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/carts")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.cartId").exists())
                .andExpect(jsonPath("data.productId").value(cartCreateDto.getProductId()))
                .andExpect(jsonPath("data.quantity").value(cartCreateDto.getQuantity()))
                .andExpect(jsonPath("data.thumbnail").value(product.getBaseProductThumbnailImage()))
                .andExpect(jsonPath("data.name").value(product.getBaseProductName()))
                .andExpect(jsonPath("data.discountPercent").value(product.getDiscountPercent()))
                .andExpect(jsonPath("data.discountPrice").value(product.getDiscountPrice()))
                .andExpect(jsonPath("data.price").value(product.getPrice()))
                .andExpect(jsonPath("data.originPrice").value(product.getOriginPrice()))
                .andExpect(jsonPath("data.stock").value(product.getStock()))
                .andDo(document("create-cart",
                        requestFields(
                                fieldWithPath("quantity").description("장바구니 수량 1 ~ 100"),
                                fieldWithPath("productId").description("장바구니에 추가할 상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.cartId").description("장바구니 고유 번호"),
                                fieldWithPath("data.productId").description("상품 고유 번호"),
                                fieldWithPath("data.quantity").description("장바구니 수량"),
                                fieldWithPath("data.thumbnail").description("썸네일 이미지 / 배열"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.discountPercent").description("상품 할인률"),
                                fieldWithPath("data.discountPrice").description("상품 할인되는 가격"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.stock").description("상품 재고량")
                        )
                ));

    }

    @Test
    @DisplayName("/api/carts 장바구니 조회")
    void queryCartSuccessTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .quantity(10)
                .productId(product.getId())
                .build();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        cartService.createCart(cartCreateDto, member);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/carts")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data[0].cartId").exists())
                .andExpect(jsonPath("data[0].productId").value(cartCreateDto.getProductId()))
                .andExpect(jsonPath("data[0].quantity").value(cartCreateDto.getQuantity()))
                .andExpect(jsonPath("data[0].thumbnail").value(product.getBaseProductThumbnailImage()))
                .andExpect(jsonPath("data[0].name").value(product.getBaseProductName()))
                .andExpect(jsonPath("data[0].discountPercent").value(product.getDiscountPercent()))
                .andExpect(jsonPath("data[0].discountPrice").value(product.getDiscountPrice()))
                .andExpect(jsonPath("data[0].price").value(product.getPrice()))
                .andExpect(jsonPath("data[0].originPrice").value(product.getOriginPrice()))
                .andExpect(jsonPath("data[0].stock").value(product.getStock()))
                .andDo(document("query-carts",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[].cartId").description("장바구니 고유 번호"),
                                fieldWithPath("data[].productId").description("상품 고유 번호"),
                                fieldWithPath("data[].quantity").description("장바구니 수량"),
                                fieldWithPath("data[].thumbnail").description("썸네일 이미지 / 배열"),
                                fieldWithPath("data[].name").description("상품 이름"),
                                fieldWithPath("data[].discountPercent").description("상품 할인률"),
                                fieldWithPath("data[].discountPrice").description("상품 할인되는 가격"),
                                fieldWithPath("data[].price").description("상품 가격"),
                                fieldWithPath("data[].originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data[].stock").description("상품 재고량"),
                                fieldWithPath("data[].expiredDate").description("유통기한 만료일").optional().type("YYYY-MM-DD")

                        )
                ));

    }

    @Test
    @DisplayName("/api/carts 장바구니 수정")
    void updateCartSuccessTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .quantity(10)
                .productId(product.getId())
                .build();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartService.createCart(cartCreateDto, member);

        CartQuantityModifyDto cartQuantityModifyDto = CartQuantityModifyDto.builder()
                .quantity(80)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/carts/{cartId}", cart.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartQuantityModifyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-carts-quantity",
                        pathParameters(
                                parameterWithName("cartId").description("장바구니 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("quantity").description("변경할 수량")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));

        assertEquals(cart.getQuantity(), cartQuantityModifyDto.getQuantity());
    }

    @Test
    @DisplayName("/api/carts/{cartId} 장바구니 삭제")
    void removeCartSuccessTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder().build();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Product product = products.get(0);
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .quantity(10)
                .productId(product.getId())
                .build();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartService.createCart(cartCreateDto, member);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/carts/{cartId}", cart.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-cart",
                        pathParameters(
                                parameterWithName("cartId").description("장바구니 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));

        assertThrows(NoExistIdException.class, () -> cartRepository.findById(cart.getId()).orElseThrow(NoExistIdException::new));
    }


    @Test
    @DisplayName("/api/carts 장바구니 여러개 삭제")
    void removeCartsSuccessTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder().build();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        List<Long> deleteIds = new ArrayList<>();
        for (Product product : products) {
            CartCreateDto cartCreateDto = CartCreateDto.builder()
                    .quantity(10)
                    .productId(product.getId())
                    .build();
            Cart cart = cartService.createCart(cartCreateDto, member);
            deleteIds.add(cart.getId());
        }

        OnlyIdsDto onlyIdsDto = OnlyIdsDto.builder()
                .ids(deleteIds)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/carts")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyIdsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-carts",
                        requestFields(
                                fieldWithPath("ids[]").description("장바구니 고유 번호 리스트")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));
    }


}