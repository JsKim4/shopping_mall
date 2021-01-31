package me.kjs.mall.api.product;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.category.Category;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.product.util.ProductSortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommonProductApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/products 판매 상품 조회 성공")
    void queryProductsTest() throws Exception {

        List<Category> parentCategory = categoryRepository.findAllByParentCategory(null);
        List<Category> chideCategory = parentCategory.get(0).getCategories();


        ProductSearchCondition base = ProductSearchCondition.builder()
                .categoryId(chideCategory.get(0).getId())
                .keyword("PRODUCT")
                .productSortType(ProductSortType.NAME)
                .build();

        Member member = memberRepository.findByEmail("user001").orElseThrow(EntityNotFoundException::new);

        TokenDto tokenDto = getTokenDto(member.getId());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .param("categoryId", base.getCategoryId() + "")
                .param("keyword", "")
                .param("page", "0")
                .param("contents", "10")
                .param("productSortType", base.getProductSortType().name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.contents[*].productId").exists())
                .andExpect(jsonPath("data.contents[*].originPrice").exists())
                .andExpect(jsonPath("data.contents[*].discountPrice").exists())
                .andExpect(jsonPath("data.contents[*].price").exists())
                .andExpect(jsonPath("data.contents[*].discountPercent").exists())
                .andExpect(jsonPath("data.contents[*].discountType").exists())
                .andExpect(jsonPath("data.contents[*].productStatus").exists())
                .andExpect(jsonPath("data.contents[*].salesBeginDate").exists())
                .andExpect(jsonPath("data.contents[*].salesEndDate").exists())
                .andExpect(jsonPath("data.contents[*].stock").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.baseProductId").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.name").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.code").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.status").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.originPrice").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.contents").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.tags").isArray())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.deliveryYn").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.bundleYn").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.deliveryType").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.returnLocation").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.fee").exists())
                .andExpect(jsonPath("data.contents[*].baseProduct.productDelivery.feeCondition").exists())
                .andDo(document(
                        "query-products",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("productSortType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_SORT_TYPE)).optional(),
                                parameterWithName("categoryId").description("카테고리 고유 번호").optional(),
                                parameterWithName("keyword").description("검색 키워드 (상품명)").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 공지 개수"),
                                fieldWithPath("data.totalCount").description("전체 공지 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].productId").description("판매 상품 고유 번호"),
                                fieldWithPath("data.contents[*].originPrice").description("소비자 가격"),
                                fieldWithPath("data.contents[*].discountPrice").description("할인되는 가격"),
                                fieldWithPath("data.contents[*].price").description("판매 가격"),
                                fieldWithPath("data.contents[*].discountPercent").description("할인 률"),
                                fieldWithPath("data.contents[*].discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE, "할인 타입")),
                                fieldWithPath("data.contents[*].productStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "상품 상태")),
                                fieldWithPath("data.contents[*].salesBeginDate").description("판매 시작일").type("Date Time"),
                                fieldWithPath("data.contents[*].salesEndDate").description("판매 종료일").type("Date Time"),
                                fieldWithPath("data.contents[*].orderCount").description("상품 주문 횟수"),
                                fieldWithPath("data.contents[*].stock").description("재고량"),
                                fieldWithPath("data.contents[*].wishYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "찜 목록에 포함된 상품")).optional(),
                                fieldWithPath("data.contents[*].evaluation.countScore1").description("1점 개수"),
                                fieldWithPath("data.contents[*].evaluation.countScore2").description("2점 개수"),
                                fieldWithPath("data.contents[*].evaluation.countScore3").description("3점 개수"),
                                fieldWithPath("data.contents[*].evaluation.countScore4").description("4점 개수"),
                                fieldWithPath("data.contents[*].evaluation.countScore5").description("5점 개수"),
                                fieldWithPath("data.contents[*].evaluation.scoreCount").description("전체 개수"),
                                fieldWithPath("data.contents[*].evaluation.qnaCount").description("qna 개수"),
                                fieldWithPath("data.contents[*].evaluation.averageScore").description("평균 별점"),
                                fieldWithPath("data.contents[*].baseProduct.baseProductId").description("기본 상품 정보 고유 번호"),
                                fieldWithPath("data.contents[*].baseProduct.name").description("상품 이름"),
                                fieldWithPath("data.contents[*].baseProduct.code").description("상품 코드"),
                                fieldWithPath("data.contents[*].baseProduct.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "상품 상태")),
                                fieldWithPath("data.contents[*].baseProduct.originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.contents[*].baseProduct.contents").description("상품 상세 정보 html"),
                                fieldWithPath("data.contents[*].baseProduct.tags").description("상품 태그 목록"),
                                fieldWithPath("data.contents[*].baseProduct.thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.returnLocation").description("반품 배송지"),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.fee").description("배송비"),
                                fieldWithPath("data.contents[*].baseProduct.productDelivery.feeCondition").description("조건부 무료 최소 배송비"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice").description("상품정보 제공고시"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.foodType").description("식품의 유형"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.manufacturer").description("제조업소"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.location").description("소재지"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.manufacturingDate").description("제조년월일"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.shelfLifeDate").description("유통기한"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.nutritionInfo").description("영양 정보"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.functionInfo").description("기능 정보"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.certificationAssociation").description("인증 기관"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.certificationCode").description("인증 코드"),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("data.contents[*].baseProduct.provisionNotice.csTel").description("소비자 상담 관련 전화번호")
                        )


                ));
    }

    @Test
    @DisplayName("/api/products/{productId} 판매 상품 단일 조회 성공")
    void queryProductTest() throws Exception {

        Product product = productRepository.findAll().stream().filter(p -> p.isUsed()).findFirst().orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/{productId}", product.getId())
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.productId").exists())
                .andExpect(jsonPath("data.originPrice").exists())
                .andExpect(jsonPath("data.discountPrice").exists())
                .andExpect(jsonPath("data.price").exists())
                .andExpect(jsonPath("data.discountPercent").exists())
                .andExpect(jsonPath("data.discountType").exists())
                .andExpect(jsonPath("data.productStatus").exists())
                .andExpect(jsonPath("data.salesBeginDate").exists())
                .andExpect(jsonPath("data.salesEndDate").exists())
                .andExpect(jsonPath("data.stock").exists())
                .andExpect(jsonPath("data.baseProduct.baseProductId").exists())
                .andExpect(jsonPath("data.baseProduct.name").exists())
                .andExpect(jsonPath("data.baseProduct.code").exists())
                .andExpect(jsonPath("data.baseProduct.status").exists())
                .andExpect(jsonPath("data.baseProduct.originPrice").exists())
                .andExpect(jsonPath("data.baseProduct.contents").exists())
                .andExpect(jsonPath("data.baseProduct.tags").isArray())
                .andExpect(jsonPath("data.baseProduct.productDelivery.deliveryYn").exists())
                .andExpect(jsonPath("data.baseProduct.productDelivery.bundleYn").exists())
                .andExpect(jsonPath("data.baseProduct.productDelivery.deliveryType").exists())
                .andExpect(jsonPath("data.baseProduct.productDelivery.returnLocation").exists())
                .andExpect(jsonPath("data.baseProduct.productDelivery.fee").exists())
                .andExpect(jsonPath("data.baseProduct.productDelivery.feeCondition").exists())
                .andDo(document(
                        "query-product",
                        pathParameters(
                                parameterWithName("productId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.productId").description("판매 상품 고유 번호"),
                                fieldWithPath("data.originPrice").description("소비자 가격"),
                                fieldWithPath("data.discountPrice").description("할인되는 가격"),
                                fieldWithPath("data.price").description("판매 가격"),
                                fieldWithPath("data.discountPercent").description("할인 률"),
                                fieldWithPath("data.discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("data.productStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "상품 상태")),
                                fieldWithPath("data.salesBeginDate").description("판매 시작일").type("Date Time"),
                                fieldWithPath("data.salesEndDate").description("판매 종료일").type("Date Time"),
                                fieldWithPath("data.expiredDate").description("유통기한 만료일").optional().type("Date"),
                                fieldWithPath("data.orderCount").description("상품 주문 횟수"),
                                fieldWithPath("data.stock").description("재고량"),
                                fieldWithPath("data.wishYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "찜 목록에 포함된 상품")),
                                fieldWithPath("data.evaluation.countScore1").description("1점 개수"),
                                fieldWithPath("data.evaluation.countScore2").description("2점 개수"),
                                fieldWithPath("data.evaluation.countScore3").description("3점 개수"),
                                fieldWithPath("data.evaluation.countScore4").description("4점 개수"),
                                fieldWithPath("data.evaluation.countScore5").description("5점 개수"),
                                fieldWithPath("data.evaluation.scoreCount").description("전체 개수"),
                                fieldWithPath("data.evaluation.averageScore").description("평균 별점"),
                                fieldWithPath("data.evaluation.qnaCount").description("qna 개수"),
                                fieldWithPath("data.baseProduct.baseProductId").description("기본 상품 정보 고유 번호"),
                                fieldWithPath("data.baseProduct.name").description("상품 이름"),
                                fieldWithPath("data.baseProduct.code").description("상품 코드"),
                                fieldWithPath("data.baseProduct.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "상품 상태")),
                                fieldWithPath("data.baseProduct.originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.baseProduct.contents").description("상품 상세 정보 html"),
                                fieldWithPath("data.baseProduct.tags").description("상품 태그 목록"),
                                fieldWithPath("data.baseProduct.thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("data.baseProduct.productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")),
                                fieldWithPath("data.baseProduct.productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")),
                                fieldWithPath("data.baseProduct.productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("data.baseProduct.productDelivery.returnLocation").description("반품 배송지"),
                                fieldWithPath("data.baseProduct.productDelivery.fee").description("배송비"),
                                fieldWithPath("data.baseProduct.productDelivery.feeCondition").description("조건부 무료 최소 배송비"),
                                fieldWithPath("data.baseProduct.provisionNotice").description("상품정보 제공고시"),
                                fieldWithPath("data.baseProduct.provisionNotice.provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("data.baseProduct.provisionNotice.foodType").description("식품의 유형"),
                                fieldWithPath("data.baseProduct.provisionNotice.manufacturer").description("제조업소"),
                                fieldWithPath("data.baseProduct.provisionNotice.location").description("소재지"),
                                fieldWithPath("data.baseProduct.provisionNotice.manufacturingDate").description("제조년월일"),
                                fieldWithPath("data.baseProduct.provisionNotice.shelfLifeDate").description("유통기한"),
                                fieldWithPath("data.baseProduct.provisionNotice.capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("data.baseProduct.provisionNotice.quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("data.baseProduct.provisionNotice.rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("data.baseProduct.provisionNotice.nutritionInfo").description("영양 정보"),
                                fieldWithPath("data.baseProduct.provisionNotice.functionInfo").description("기능 정보"),
                                fieldWithPath("data.baseProduct.provisionNotice.intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("data.baseProduct.provisionNotice.noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("data.baseProduct.provisionNotice.certificationAssociation").description("인증 기관"),
                                fieldWithPath("data.baseProduct.provisionNotice.certificationCode").description("인증 코드"),
                                fieldWithPath("data.baseProduct.provisionNotice.gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("data.baseProduct.provisionNotice.importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("data.baseProduct.provisionNotice.csTel").description("소비자 상담 관련 전화번호")
                        )


                ));
    }
}