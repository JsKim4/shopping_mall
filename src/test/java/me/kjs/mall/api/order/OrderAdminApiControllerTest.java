package me.kjs.mall.api.order;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderAdminSearchCondition;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class OrderAdminApiControllerTest extends BaseTest {

    @Test
    void ordersQueryTest() throws Exception {
        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        em.flush();
        em.clear();
        OrderAdminSearchCondition searchCondition = OrderAdminSearchCondition.builder()
                .page(0)
                .contents(10)
                .orderBeginDate(LocalDate.now().minusDays(1))
                .orderEndDate(LocalDate.now().plusDays(1))
                .build();
        testService.orderProductModifyState(order.getOrderSpecifics().get(0).getOrderProducts().get(0).getId(), OrderState.ORDER_ACCEPT);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/admin")
                .param("page", String.valueOf(searchCondition.getPage()))
                .param("orderBeginDate", String.valueOf(searchCondition.getOrderBeginDate()))
                .param("orderEndDate", String.valueOf(searchCondition.getOrderEndDate()))
                .param("contents", String.valueOf(searchCondition.getContents()))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("query-admin-orders",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("orderBeginDate").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("orderEndDate").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상품 상태 코드")).optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.contents[*].productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].memberId").description("회원 고유 번호"),
                                fieldWithPath("data.contents[*].orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.contents[*].orderId").description("주문 고유 번호"),
                                fieldWithPath("data.contents[*].orderProductCode").description("주문 상품 코드"),
                                fieldWithPath("data.contents[*].orderCode").description("주문 코드"),
                                fieldWithPath("data.contents[*].orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상태 코드")),
                                fieldWithPath("data.contents[*].productCode").description("상품 코드"),
                                fieldWithPath("data.contents[*].productName").description("상품 이름"),
                                fieldWithPath("data.contents[*].quantity").description("상품 개수"),
                                fieldWithPath("data.contents[*].memberName").description("주문자 이름"),
                                fieldWithPath("data.contents[*].memberEmail").description("주문자 이메일"),
                                fieldWithPath("data.contents[*].recipient").description("수령인 이름"),
                                fieldWithPath("data.contents[*].orderDateTime").description("주문 일시")
                        )));

    }

    @Test
    void orderQueryTest() throws Exception {
        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        em.flush();
        em.clear();
        OrderAdminSearchCondition searchCondition = OrderAdminSearchCondition.builder()
                .page(0)
                .contents(10)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/admin/{orderProductId}", order.getOrderSpecifics().get(0).getOrderProducts().get(0).getId())
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("query-admin-order",
                        pathParameters(
                                parameterWithName("orderProductId").description("주문 상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderProductCode").description("주문 상품 코드"),
                                fieldWithPath("data.orderSpecificId").description("주문 고유 번호"),
                                fieldWithPath("data.orderCode").description("주문 코드"),
                                fieldWithPath("data.productName").description("주문 상품명"),
                                fieldWithPath("data.orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "현재 상태")),
                                fieldWithPath("data.orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "현재 상태")),
                                fieldWithPath("data.memberEmail").description("주문자 이메일"),
                                fieldWithPath("data.quantity").description("개수"),
                                fieldWithPath("data.discountPrice").description("할인 금액"),
                                fieldWithPath("data.sumOriginPrice").description("금액 합계"),
                                fieldWithPath("data.paymentPrice").description("결제 금액"),
                                fieldWithPath("data.orderProductLogHistories.").description("주문 상품 상태 변경 이력"),
                                fieldWithPath("data.orderDestination.message").description("배송 메세지"),
                                fieldWithPath("data.orderDestination.recipient").description("수령인 이름"),
                                fieldWithPath("data.orderDestination.tel1").description("전화번호 1"),
                                fieldWithPath("data.orderDestination.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.orderDestination.addressSimple").description("수령 주소"),
                                fieldWithPath("data.orderDestination.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.orderDestination.zipcode").description("우편 번호"),
                                fieldWithPath("data.orderProductLogHistories[*].postOrderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "변경 주문 상태")),
                                fieldWithPath("data.orderProductLogHistories[*].preOrderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "변경 주문 상태")).optional(),
                                fieldWithPath("data.orderProductLogHistories[*].createDateTime").description("변경 일시"),
                                fieldWithPath("data.orderProductLogHistories[*].createdMemberName").description("수정한 사람")
                        )));

    }


    @Test
    void placeDownloadTest() throws Exception {
        TokenDto tokenDto = getUserTokenDto();
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            OrderCreateDto orderCreateDto = generateOrderCreateDto();
            Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
            Order order = orderService.createOrder(orderCreateDto, member.getId());
            for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
                ids.add(orderSpecific.getId());
                for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                    orderProduct.modifyOrderState(OrderState.PAYMENT_ACCEPT);
                }
            }
        }
        OrderCreateDto orderCreateDto = generateOrderCreateDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        ids.add(order.getOrderSpecifics().get(0).getId());


        em.flush();
        em.clear();

        OnlyIdsDto onlyIdsDto = OnlyIdsDto.builder()
                .ids(ids)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders/admin/place/download")
                .content(objectMapper.writeValueAsString(onlyIdsDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("create-admin-order-place",
                        requestFields(
                                fieldWithPath("ids[*]").description("주문 상세 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.successes").description("정상적으로 발주 가능한 주문 정보"),
                                fieldWithPath("data.successes[*].주문번호").description("주문번호"),
                                fieldWithPath("data.successes[*].상품주문번호").description("상품주문번호"),
                                fieldWithPath("data.successes[*].상품코드").description("상품코드"),
                                fieldWithPath("data.successes[*].주문수량").description("주문수량"),
                                fieldWithPath("data.successes[*].주문액").description("주문액"),
                                fieldWithPath("data.successes[*].수취인").description("수취인"),
                                fieldWithPath("data.successes[*].전화번호1").description("전화번호1"),
                                fieldWithPath("data.successes[*].전화번호2").description("전화번호2"),
                                fieldWithPath("data.successes[*].배송주소").description("배송주소"),
                                fieldWithPath("data.successes[*].배송메시지").description("배송메시지"),
                                fieldWithPath("data.successes[*].결제금액").description("결제금액"),
                                fieldWithPath("data.successes[*].사용포인트").description("사용포인트"),
                                fieldWithPath("data.successes[*].결제일자").description("결제일자"),
                                fieldWithPath("data.successes[*].").description("배송 메시지"),
                                fieldWithPath("data.failures").description("정상적으로 발주 불가능한 주문 정보"),
                                fieldWithPath("data.failures[*].orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.failures[*].orderCode").description("주문 코드"),
                                fieldWithPath("data.failures[*].productCode").description("상품 코드"),
                                fieldWithPath("data.failures[*].productName").description("상품 이름"),
                                fieldWithPath("data.failures[*].quantity").description("주문 개수"),
                                fieldWithPath("data.failures[*].price").description("주문 총 금액 (소비자가격)"),
                                fieldWithPath("data.failures[*].recipient").description("수령인"),
                                fieldWithPath("data.failures[*].tel1").description("전화번호1"),
                                fieldWithPath("data.failures[*].tel2").description("전화번호2").optional(),
                                fieldWithPath("data.failures[*].address1").description("배송지"),
                                fieldWithPath("data.failures[*].message").description("배송 메시지"),
                                fieldWithPath("data.failures[*].failCause").description("정상 발주 실패 사유")
                        )));

    }

    @Test
    void placeAcceptTest() throws Exception {
        TokenDto tokenDto = getUserTokenDto();
        Long orderSpecificId = null;
        Order order = null;
        for (int i = 0; i < 1; i++) {
            OrderCreateDto orderCreateDto = generateOrderCreateDto();
            Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
            order = orderService.createOrder(orderCreateDto, member.getId());
            for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
                orderSpecificId = orderSpecific.getId();
                for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                    orderProduct.modifyOrderState(OrderState.PAYMENT_ACCEPT);
                }
            }
            order.paymentAccept();
        }
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/admin/place/accept/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-admin-order-place-accept",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        )));

        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            Assertions.assertEquals(orderSpecific.getOrderState(), OrderState.DELIVERY_WAIT);
            for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                Assertions.assertEquals(orderProduct.getOrderProductState(), OrderState.DELIVERY_WAIT);
            }
        }
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
                .orderMultipleYn(YnType.N)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();
    }


}