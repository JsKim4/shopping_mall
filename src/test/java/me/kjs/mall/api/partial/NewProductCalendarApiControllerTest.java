package me.kjs.mall.api.partial;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.partial.NewProductCalendar;
import me.kjs.mall.partial.dto.NewProductCalendarCreateDto;
import me.kjs.mall.partial.dto.NewProductCreateDto;
import me.kjs.mall.partial.dto.NewProductSearchCondition;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.product.util.ProductSortType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class NewProductCalendarApiControllerTest extends BaseTest {

    @Test
    void updateCalendarTest() throws Exception {

        List<NewProductCreateDto> newProductCreateDtos = new ArrayList<>();
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        for (int i = 0; i < 5 && i < products.size(); i++) {
            newProductCreateDtos.add(
                    NewProductCreateDto.builder()
                            .beginDate(begin)
                            .endDate(end)
                            .productId(products.get(i).getId())
                            .build()
            );
            begin = begin.plusDays(1);
            end = end.plusDays(1);
        }
        NewProductCalendarCreateDto newProductCalendarCreateDto = NewProductCalendarCreateDto.builder().newProducts(newProductCreateDtos).build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/new/product/calendar/{newProductCalendarId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProductCalendarCreateDto))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(201))
                .andDo(document("update-new_product_calendar",
                        pathParameters(
                                parameterWithName("newProductCalendarId").description("신규 상품 캘린더 번호 / 순서와 동일")
                        ),
                        requestFields(
                                fieldWithPath("newProducts").description("신규 상품 메인노출 설정 리스트"),
                                fieldWithPath("newProducts[*].beginDate").description("시작일"),
                                fieldWithPath("newProducts[*].endDate").description("종료일"),
                                fieldWithPath("newProducts[*].productId").description("상품 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.calendarId").description("신규 상품 캘린터 고유 번호 (순서와 동일)"),
                                fieldWithPath("data.newProducts[*].beginDate").description("상품 메인노출 종료 일자"),
                                fieldWithPath("data.newProducts[*].endDate").description("상품 메인노출 종료 일자"),
                                fieldWithPath("data.newProducts[*].productInfo").description("상품 정보"),
                                fieldWithPath("data.newProducts[*].productInfo.productId").description("상품 고유 번호"),
                                fieldWithPath("data.newProducts[*].productInfo.name").description("상품 이름"),
                                fieldWithPath("data.newProducts[*].productInfo.thumbnailImage").description("상품 썸네일 이미지 리스트"),
                                fieldWithPath("data.newProducts[*].productInfo.originPrice").description("원가격"),
                                fieldWithPath("data.newProducts[*].productInfo.stock").description("재고 수량"),
                                fieldWithPath("data.newProducts[*].productInfo.commonStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.newProducts[*].productInfo.salesBeginDate").description("판매 시작일"),
                                fieldWithPath("data.newProducts[*].productInfo.salesEndDate").description("판매 종료일"),
                                fieldWithPath("data.newProducts[*].productInfo.discountPrice").description("할인 가격"),
                                fieldWithPath("data.newProducts[*].productInfo.discountPercent").description("할인율"),
                                fieldWithPath("data.newProducts[*].productInfo.price").description("판매 가격")
                        )
                ));
        List<NewProductCalendar> newProductCalendars = partialService.findAllNewProductCalendarFetch();
    }

    @Test
    void queryNowNewProducts() throws Exception {
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder()
                .productSortType(ProductSortType.NAME).build()).getContents();
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        for (int i = 0; i < 5 && i < products.size(); i++) {
            List<NewProductCreateDto> newProductCreateDtos = new ArrayList<>();
            newProductCreateDtos.add(
                    NewProductCreateDto.builder()
                            .beginDate(begin)
                            .endDate(end)
                            .productId(products.get(i).getId())
                            .build()
            );
            NewProductCalendarCreateDto newProductCalendarCreateDto = NewProductCalendarCreateDto.builder().newProducts(newProductCreateDtos).build();
            partialService.updateNewProductCalendar(i + 1, newProductCalendarCreateDto);
        }

        NewProductSearchCondition newProductSearchCondition = NewProductSearchCondition.builder()
                .contents(10)
                .page(0)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/new/product/now")
                .param("page", String.valueOf(newProductSearchCondition.getPage()))
                .param("contents", String.valueOf(newProductSearchCondition.getContents())))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("query-new_product-now",
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

    @Test
    void queryNewProductCalendar() throws Exception {
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder()
                .productSortType(ProductSortType.NAME)
                .build()).getContents();
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        for (int i = 0; i < 5 && i < products.size(); i++) {
            List<NewProductCreateDto> newProductCreateDtos = new ArrayList<>();
            newProductCreateDtos.add(
                    NewProductCreateDto.builder()
                            .beginDate(begin)
                            .endDate(end)
                            .productId(products.get(i).getId())
                            .build()
            );
            newProductCreateDtos.add(
                    NewProductCreateDto.builder()
                            .beginDate(begin.plusDays(1))
                            .endDate(end.plusDays(6))
                            .productId(products.get(i + 1).getId())
                            .build()
            );
            NewProductCalendarCreateDto newProductCalendarCreateDto = NewProductCalendarCreateDto.builder().newProducts(newProductCreateDtos).build();
            partialService.updateNewProductCalendar(i + 1, newProductCalendarCreateDto);
        }

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/new/product/calendar")
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("query-new_product_calendar",
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[*].calendarId").description("신규 상품 캘린터 고유 번호 (순서와 동일)"),
                                fieldWithPath("data[*].newProducts[*].beginDate").description("상품 메인노출 종료 일자"),
                                fieldWithPath("data[*].newProducts[*].endDate").description("상품 메인노출 종료 일자"),
                                fieldWithPath("data[*].newProducts[*].productInfo").description("상품 정보"),
                                fieldWithPath("data[*].newProducts[*].productInfo.productId").description("상품 고유 번호"),
                                fieldWithPath("data[*].newProducts[*].productInfo.name").description("상품 이름"),
                                fieldWithPath("data[*].newProducts[*].productInfo.thumbnailImage").description("상품 썸네일 이미지 리스트"),
                                fieldWithPath("data[*].newProducts[*].productInfo.originPrice").description("원가격"),
                                fieldWithPath("data[*].newProducts[*].productInfo.stock").description("재고 수량"),
                                fieldWithPath("data[*].newProducts[*].productInfo.commonStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data[*].newProducts[*].productInfo.salesBeginDate").description("판매 시작일"),
                                fieldWithPath("data[*].newProducts[*].productInfo.salesEndDate").description("판매 종료일"),
                                fieldWithPath("data[*].newProducts[*].productInfo.discountPrice").description("할인 가격"),
                                fieldWithPath("data[*].newProducts[*].productInfo.discountPercent").description("할인율"),
                                fieldWithPath("data[*].newProducts[*].productInfo.price").description("판매 가격")

                        )
                ));

    }

}