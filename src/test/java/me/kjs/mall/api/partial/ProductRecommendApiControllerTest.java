package me.kjs.mall.api.partial;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.CommonSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ProductRecommendApiControllerTest extends BaseTest {

    @Test
    void productRecommendTest() throws Exception {
        CommonSearchCondition commonSearchCondition = CommonSearchCondition.builder()
                .contents(4)
                .page(0)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/recommend")
                .param("contents", String.valueOf(commonSearchCondition.getContents()))
                .param("page", String.valueOf(commonSearchCondition.getPage()))
                .header("X-AUTH-TOKEN", getUserTokenDto().getToken()))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("query-product-recommend",
                        requestParameters(
                                parameterWithName("contents").description("요청할 개수(최소 1 최대 100)"),
                                parameterWithName("page").description("요청할 페이지 (0 부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("페이지 콘텐츠 개수"),
                                fieldWithPath("data.totalCount").description("전체 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].name").description("상품 이름"),
                                fieldWithPath("data.contents[*].thumbnailImage").description("썸네일 이미지 목록"),
                                fieldWithPath("data.contents[*].originPrice").description("원 판매 가격"),
                                fieldWithPath("data.contents[*].stock").description("재고량"),
                                fieldWithPath("data.contents[*].commonStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[*].salesBeginDate").description("판매 시작일"),
                                fieldWithPath("data.contents[*].salesEndDate").description("판매 종료일"),
                                fieldWithPath("data.contents[*].discountPrice").description("할인 금액"),
                                fieldWithPath("data.contents[*].discountPercent").description("할인률"),
                                fieldWithPath("data.contents[*].price").description("최종 판매 가격")
                        )
                ));
    }

}