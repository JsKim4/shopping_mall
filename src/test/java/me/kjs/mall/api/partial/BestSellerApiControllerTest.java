package me.kjs.mall.api.partial;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.partial.BestSeller;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class BestSellerApiControllerTest extends BaseTest {

    @Test
    void queryBestSeller() throws Exception {
        List<Product> allFetch = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        int count = 10;
        for (Product fetch : allFetch) {
            BestSeller bestSeller = BestSeller.createBestSeller(fetch, count, LocalDate.now());
            bestSellerRepository.save(bestSeller);
            count += 10;
        }

        CommonSearchCondition commonSearchCondition = CommonSearchCondition.builder()
                .contents(10)
                .page(0)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/best/sellers")
                .param("contents", String.valueOf(commonSearchCondition.getContents()))
                .param("page", String.valueOf(commonSearchCondition.getPage())))
                .andDo(document("query-best_seller",
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