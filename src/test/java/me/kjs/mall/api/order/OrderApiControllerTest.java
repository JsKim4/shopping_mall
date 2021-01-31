package me.kjs.mall.api.order;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderCreateByNonMemberDto;
import me.kjs.mall.order.dto.OrderNonMemberInfoDto;
import me.kjs.mall.order.dto.OrderQueryNonMemberDto;
import me.kjs.mall.order.dto.OrderSearchConditionDto;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.sheet.OrderSheetCreateDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.order.specific.product.dto.OrderExchangeRequestDto;
import me.kjs.mall.order.specific.product.dto.OrderProductIdAndQuantityDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderApiControllerTest extends BaseTest {


    @Test
    @DisplayName("/api/orders/sheet 주문서 생성 성공 테스트")
    void orderSheetCreateTest() throws Exception {
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

        OrderSheetCreateDto orderSheetCreateDto = OrderSheetCreateDto.builder()
                .products(productIdAndQuantityDtos)
                .build();

        TokenDto tokenDto = getUserTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders/sheet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderSheetCreateDto))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.products[0].productId").exists())
                .andExpect(jsonPath("data.products[0].quantity").exists())
                .andExpect(jsonPath("data.products[0].requestQuantity").exists())
                .andExpect(jsonPath("data.products[0].thumbnail").exists())
                .andExpect(jsonPath("data.products[0].name").exists())
                .andExpect(jsonPath("data.products[0].discountPercent").exists())
                .andExpect(jsonPath("data.products[0].originPrice").exists())
                .andExpect(jsonPath("data.products[0].price").exists())
                .andExpect(jsonPath("data.products[0].discountPrice").exists())
                .andExpect(jsonPath("data.products[0].sumOriginPrice").exists())
                .andExpect(jsonPath("data.products[0].sumPrice").exists())
                .andExpect(jsonPath("data.products[0].sumDiscountPrice").exists())
                .andExpect(jsonPath("data.products[0].stock").exists())
                .andExpect(jsonPath("data.paymentInfo.deliveryFee").exists())
                .andExpect(jsonPath("data.paymentInfo.originPrice").exists())
                .andExpect(jsonPath("data.paymentInfo.sumDiscountPrice").exists())
                .andExpect(jsonPath("data.paymentInfo.sumPrice").exists())
                .andDo(document(
                        "create-order-sheet",
                        requestFields(
                                fieldWithPath("products").description("주문서를 구성할 상품 리스트"),
                                fieldWithPath("products[].productId").description("주문 상품 고유 번호"),
                                fieldWithPath("products[].quantity").description("주문 상품 개수")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.products[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.products[].quantity").description("생성된 주문서의 상품 개수"),
                                fieldWithPath("data.products[].requestQuantity").description("요청한 상품 개수"),
                                fieldWithPath("data.products[].thumbnail").description("썸네일 이미지 리스트"),
                                fieldWithPath("data.products[].name").description("상품 이름"),
                                fieldWithPath("data.products[].discountPercent").description("상품 할인률"),
                                fieldWithPath("data.products[].originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.products[].price").description("상품 판매 가격"),
                                fieldWithPath("data.products[].discountPrice").description("상품 할인되는 가격"),
                                fieldWithPath("data.products[].sumOriginPrice").description("상품 소비자 가격 총합"),
                                fieldWithPath("data.products[].sumPrice").description("상품 판매 가격 총합"),
                                fieldWithPath("data.products[].sumDiscountPrice").description("상품 할인되는 가격 총합"),
                                fieldWithPath("data.products[].stock").description("상품 재고량"),
                                fieldWithPath("data.paymentInfo.deliveryFee").description("배송비"),
                                fieldWithPath("data.paymentInfo.originPrice").description("소비자 가격 상품 총합"),
                                fieldWithPath("data.paymentInfo.sumDiscountPrice").description("할인되는 가격 총합"),
                                fieldWithPath("data.paymentInfo.sumPrice").description("판매 가격 총합"),
                                fieldWithPath("data.paymentInfo.sumPaymentPrice").description("총 결제 가격")

                        )
                ));
    }


    @Test
    @DisplayName("/api/orders 주문 생성 성공 테스트")
    void orderCreateTest() throws Exception {
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

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient1")
                                .addressSimple("address1")
                                .addressDetail("addressDetail1")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build(),
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient2")
                                .addressSimple("address2")
                                .addressDetail("addressDetail2")
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


        TokenDto tokenDto = getUserTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderCreateDto))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "create-order",
                        requestFields(
                                fieldWithPath("orderMultipleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다중 주문 여부")),
                                fieldWithPath("paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "주문 유형")),
                                fieldWithPath("products").description("주문을 구성할 상품 리스트"),
                                fieldWithPath("point").description("사용 포인트"),
                                fieldWithPath("products[].productId").description("주문 상품 고유 번호"),
                                fieldWithPath("products[].quantity").description("주문 상품 개수"),
                                fieldWithPath("destination[].message").description("배송 메세지"),
                                fieldWithPath("destination[].recipient").description("수령인 이름"),
                                fieldWithPath("destination[].tel1").description("전화번호 1"),
                                fieldWithPath("destination[].tel2").description("전화번호 2").optional(),
                                fieldWithPath("destination[].addressSimple").description("수령 주소"),
                                fieldWithPath("destination[].addressDetail").description("수령 주소 상세"),
                                fieldWithPath("destination[].zipcode").description("우편 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderId").description("주문 고유 번호"),
                                fieldWithPath("data.paymentCertificationRequest").description("결제 인증 전달용 정보"),
                                fieldWithPath("data.paymentCertificationRequest.certificationRequestUrl").description("결제 인증 신청용 NICE PAY URL"),
                                fieldWithPath("data.paymentCertificationRequest.certificationRequestWebviewUrl").description("결제 인증 전달용 웹뷰 URL"),
                                fieldWithPath("data.paymentCertificationRequest.goodsName").description("상품이름"),
                                fieldWithPath("data.paymentCertificationRequest.amt").description("결제 금액"),
                                fieldWithPath("data.paymentCertificationRequest.ediDate").description("전문 생성 일시"),
                                fieldWithPath("data.paymentCertificationRequest.moid").description("상점 주문 번호"),
                                fieldWithPath("data.paymentCertificationRequest.signData").description("위변조 검증 데이터"),
                                fieldWithPath("data.paymentCertificationRequest.returnUrl").description("인증 처리 후 redirect url"),
                                fieldWithPath("data.paymentCertificationRequest.payMethod").description("결제 방법"),
                                fieldWithPath("data.paymentCertificationRequest.mid").description("상점 아이디"),
                                fieldWithPath("data.paymentCertificationRequest.vbankExpDate").description("가상계좌 입금 만료일").optional().type("String"),
                                fieldWithPath("data.orderPayment").description("주문 금액 정보"),
                                fieldWithPath("data.orderPayment.paymentPrice").description("결제 금액"),
                                fieldWithPath("data.orderPayment.paymentMethod").description("결제 방법"),
                                fieldWithPath("data.orderPayment.paymentDateTime").description("결제 일시").optional().type("Date"),
                                fieldWithPath("data.orderSpecifics").description("주문 상세 정보"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificCode").description("주문 상세 고유 코드 번호"),
                                fieldWithPath("data.orderSpecifics[].orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상세 상태")),
                                fieldWithPath("data.orderSpecifics[].orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 교환 상태")),
                                fieldWithPath("data.orderSpecifics[].deliveryApiUrl").description("배송조회 API URL").type("Stirng").optional(),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment").description("주문 상세 금액 정보"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumOriginalPrice").description("소비자 가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumDiscountPrice").description("할인 가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumPrice").description("가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.deliveryFee").description("배송비"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumPaymentPrice").description("실제 결제에 참여되는 금액 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "결제 수단")),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.paymentDateTime").description("결제 시간").type("Date Time").optional(),
                                fieldWithPath("data.orderSpecifics[].orderDestination").description("배송 정보"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.recipient").description("수령인"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.addressSimple").description("수령 주소 요약"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.tel1").description("전화번호 1"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.orderSpecifics[].orderDestination.zipcode").description("우편번호"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.message").description("배송 메세지"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[]").description("주문 상품 리스트"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].name").description("상품명"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].quantity").description("주문 수량"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].price").description("결제에 참여한 가격"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상품 상태")),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 상품 교환 상태")),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].thumbnail").description("썸네일 이미지"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].expiredDate").description("유통기한 만료일").optional().type("Date")

                        )));
    }

    @Test
    @DisplayName("/api/orders/non 비회원 주문 생성 성공 테스트")
    void orderCreateNonMemberTest() throws Exception {
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

        OrderCreateByNonMemberDto orderCreateDto = OrderCreateByNonMemberDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient1")
                                .addressSimple("address1")
                                .addressDetail("addressDetail1")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build(),
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient2")
                                .addressSimple("address2")
                                .addressDetail("addressDetail2")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build()
                ))
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .memberInfo(OrderNonMemberInfoDto
                        .builder()
                        .email("email123456")
                        .name("hong")
                        .phoneNumber("01000001111")
                        .build())
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/orders/non")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "create-order-non-member",
                        requestFields(
                                fieldWithPath("orderMultipleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다중 주문 여부")),
                                fieldWithPath("paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "주문 유형")),
                                fieldWithPath("products").description("주문을 구성할 상품 리스트"),
                                fieldWithPath("memberInfo.email").description("구매자 이메일"),
                                fieldWithPath("memberInfo.phoneNumber").description("구매자 핸드폰 번호"),
                                fieldWithPath("memberInfo.name").description("구매자 이름"),
                                fieldWithPath("products[].productId").description("주문 상품 고유 번호"),
                                fieldWithPath("products[].quantity").description("주문 상품 개수"),
                                fieldWithPath("destination[].message").description("배송 메세지"),
                                fieldWithPath("destination[].recipient").description("수령인 이름"),
                                fieldWithPath("destination[].tel1").description("전화번호 1"),
                                fieldWithPath("destination[].tel2").description("전화번호 2").optional(),
                                fieldWithPath("destination[].addressSimple").description("수령 주소"),
                                fieldWithPath("destination[].addressDetail").description("수령 주소 상세"),
                                fieldWithPath("destination[].zipcode").description("우편 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderId").description("주문 고유 번호"),
                                fieldWithPath("data.paymentCertificationRequest").description("결제 인증 전달용 정보"),
                                fieldWithPath("data.paymentCertificationRequest.certificationRequestUrl").description("결제 인증 신청용 NICE PAY URL"),
                                fieldWithPath("data.paymentCertificationRequest.certificationRequestWebviewUrl").description("결제 인증 전달용 웹뷰 URL"),
                                fieldWithPath("data.paymentCertificationRequest.goodsName").description("상품이름"),
                                fieldWithPath("data.paymentCertificationRequest.amt").description("결제 금액"),
                                fieldWithPath("data.paymentCertificationRequest.ediDate").description("전문 생성 일시"),
                                fieldWithPath("data.paymentCertificationRequest.moid").description("상점 주문 번호"),
                                fieldWithPath("data.paymentCertificationRequest.signData").description("위변조 검증 데이터"),
                                fieldWithPath("data.paymentCertificationRequest.returnUrl").description("인증 처리 후 redirect url"),
                                fieldWithPath("data.paymentCertificationRequest.payMethod").description("결제 방법"),
                                fieldWithPath("data.paymentCertificationRequest.mid").description("상점 아이디"),
                                fieldWithPath("data.paymentCertificationRequest.vbankExpDate").description("가상계좌 입금 만료일").optional().type("String"),
                                fieldWithPath("data.orderPayment").description("주문 금액 정보"),
                                fieldWithPath("data.orderPayment.paymentPrice").description("결제 금액"),
                                fieldWithPath("data.orderPayment.paymentMethod").description("결제 방법"),
                                fieldWithPath("data.orderPayment.paymentDateTime").description("결제 일시").optional().type("Date"),
                                fieldWithPath("data.orderSpecifics").description("주문 상세 정보"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificCode").description("주문 상세 고유 코드 번호"),
                                fieldWithPath("data.orderSpecifics[].orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상세 상태")),
                                fieldWithPath("data.orderSpecifics[].orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 교환 상태")),
                                fieldWithPath("data.orderSpecifics[].deliveryApiUrl").description("배송조회 API URL").type("Stirng").optional(),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment").description("주문 상세 금액 정보"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumOriginalPrice").description("소비자 가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumDiscountPrice").description("할인 가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumPrice").description("가격 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.deliveryFee").description("배송비"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.sumPaymentPrice").description("실제 결제에 참여되는 금액 총합"),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "결제 수단")),
                                fieldWithPath("data.orderSpecifics[].orderSpecificPayment.paymentDateTime").description("결제 시간").type("Date Time").optional(),
                                fieldWithPath("data.orderSpecifics[].orderDestination").description("배송 정보"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.recipient").description("수령인"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.addressSimple").description("수령 주소 요약"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.tel1").description("전화번호 1"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.orderSpecifics[].orderDestination.zipcode").description("우편번호"),
                                fieldWithPath("data.orderSpecifics[].orderDestination.message").description("배송 메세지"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[]").description("주문 상품 리스트"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].name").description("상품명"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].quantity").description("주문 수량"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].price").description("결제에 참여한 가격"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상품 상태")),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].orderProductExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 상품 교환 상태")),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].thumbnail").description("썸네일 이미지"),
                                fieldWithPath("data.orderSpecifics[].orderProducts[].expiredDate").description("유통기한 만료일").optional().type("Date")

                        )));
    }


    @Test
    @DisplayName("/api/orders/non 비회원 주문 조회 테스트")
    void orderQueryNonMemberTest() throws Exception {
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

        String name = "hong";
        OrderCreateByNonMemberDto orderCreateDto = OrderCreateByNonMemberDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient1")
                                .addressSimple("address1")
                                .addressDetail("addressDetail1")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build(),
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient2")
                                .addressSimple("address2")
                                .addressDetail("addressDetail2")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build()
                ))
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .memberInfo(OrderNonMemberInfoDto
                        .builder()
                        .email("email123456")
                        .name(name)
                        .phoneNumber("01000001111")
                        .build())
                .build();

        Order order = orderService.createOrder(orderCreateDto);

        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            orderSpecific.paymentAccept();
        }
        OrderQueryNonMemberDto orderQueryNonMemberDto = OrderQueryNonMemberDto.builder()
                .name(name)
                .orderCode(order.getOrderSpecifics().get(0).getOrderCode())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/non")
                .param("name", orderQueryNonMemberDto.getName())
                .param("orderCode", orderQueryNonMemberDto.getOrderCode()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "query-order-non-member",
                        requestParameters(
                                parameterWithName("name").description("구매자 이름"),
                                parameterWithName("orderCode").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.orderSpecificCode").description("주문 상세 고유 코드 번호"),
                                fieldWithPath("data.orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상세 상태")),
                                fieldWithPath("data.orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 교환 상태")),
                                fieldWithPath("data.deliveryApiUrl").description("배송조회 API URL").type("Stirng").optional(),
                                fieldWithPath("data.orderSpecificPayment").description("주문 상세 금액 정보"),
                                fieldWithPath("data.orderSpecificPayment.sumOriginalPrice").description("소비자 가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.sumDiscountPrice").description("할인 가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.sumPrice").description("가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.deliveryFee").description("배송비"),
                                fieldWithPath("data.orderSpecificPayment.sumPaymentPrice").description("실제 결제에 참여되는 금액 총합"),
                                fieldWithPath("data.orderSpecificPayment.paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "결제 수단")),
                                fieldWithPath("data.orderSpecificPayment.paymentDateTime").description("결제 시간").type("Date Time").optional(),
                                fieldWithPath("data.orderDestination").description("배송 정보"),
                                fieldWithPath("data.orderDestination.recipient").description("수령인"),
                                fieldWithPath("data.orderDestination.addressSimple").description("수령 주소 요약"),
                                fieldWithPath("data.orderDestination.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.orderDestination.tel1").description("전화번호 1"),
                                fieldWithPath("data.orderDestination.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.orderDestination.zipcode").description("우편번호"),
                                fieldWithPath("data.orderDestination.message").description("배송 메세지"),
                                fieldWithPath("data.orderProducts[]").description("주문 상품 리스트"),
                                fieldWithPath("data.orderProducts[].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderProducts[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.orderProducts[].name").description("상품명"),
                                fieldWithPath("data.orderProducts[].quantity").description("주문 수량"),
                                fieldWithPath("data.orderProducts[].price").description("결제에 참여한 가격"),
                                fieldWithPath("data.orderProducts[].orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상품 상태")),
                                fieldWithPath("data.orderProducts[].orderProductExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 상품 교환 상태")),
                                fieldWithPath("data.orderProducts[].thumbnail").description("썸네일 이미지"),
                                fieldWithPath("data.orderProducts[].expiredDate").description("유통기한 만료일").optional().type("Date")

                        )));
    }


    @Test
    @DisplayName("/api/orders 주문 리스트 조회 성공 테스트")
    void orderSpecificsQueryTest() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        for (int i = 0; i < 2; i++) {
            Order order = orderService.createOrder(orderCreateDto, member.getId());
            em.flush();
            for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
                testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.PAYMENT_ACCEPT);
                for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                    testService.orderProductModifyState(orderProduct.getId(), OrderState.PAYMENT_ACCEPT);
                }
            }
        }

        OrderSearchConditionDto orderSearchConditionDto = OrderSearchConditionDto.builder()
                .contents(2)
                .orderState(OrderState.PAYMENT_ACCEPT)
                .page(0)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .param("contents", String.valueOf(orderSearchConditionDto.getContents()))
                .param("orderState", String.valueOf(orderSearchConditionDto.getOrderState()))
                .param("page", String.valueOf(orderSearchConditionDto.getPage())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.contentCount").value(orderSearchConditionDto.getContents()))
                .andExpect(jsonPath("data.nowPage").value(orderSearchConditionDto.getPage()))
                .andExpect(jsonPath("data.hasNext").value(YnType.N.name()))
                .andExpect(jsonPath("data.contents[0].orderSpecificId").exists())
                .andExpect(jsonPath("data.contents[0].orderSpecificCode").exists())
                .andExpect(jsonPath("data.contents[0].orderName").exists())
                .andExpect(jsonPath("data.contents[0].orderDate").exists())
                .andExpect(jsonPath("data.contents[0].sumPaymentPrice").exists())
                .andExpect(jsonPath("data.contents[0].thumbnail").isArray())
                .andExpect(jsonPath("data.contents[0].orderState").exists())
                .andDo(document(
                        "query-orders",
                        requestParameters(
                                parameterWithName("contents").description("요청할 개수(최소 1 최대 30)"),
                                parameterWithName("orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "null 일경우 전체검색")),
                                parameterWithName("page").description("요청할 페이지 (0 부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("요청한 콘텐츠 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.hasNext").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다음 페이지 존재 여부")),
                                fieldWithPath("data.contents[].orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.contents[].orderSpecificCode").description("주문 상세 고유 코드 번호"),
                                fieldWithPath("data.contents[].orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상세 상태")),
                                fieldWithPath("data.contents[].orderName").description("주문 이름"),
                                fieldWithPath("data.contents[].orderDate").description("주문일"),
                                fieldWithPath("data.contents[].sumPaymentPrice").description("결제 금액"),
                                fieldWithPath("data.contents[].thumbnail").description("상품 이미지 리스트"),
                                fieldWithPath("data.contents[].deliveryApiUrl").description("배송조회 API URL").type("Stirng").optional()
                        )

                ));
    }


    @Test
    @DisplayName("/api/orders/{orderSpecificId} 주문 조회 성공 테스트")
    void orderSpecificQueryTest() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        order.paymentAccept();

        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.orderSpecificId").exists())
                .andExpect(jsonPath("data.orderSpecificCode").exists())
                .andExpect(jsonPath("data.orderState").exists())
                .andExpect(jsonPath("data.orderSpecificPayment").exists())
                .andExpect(jsonPath("data.orderSpecificPayment.sumOriginalPrice").exists())
                .andExpect(jsonPath("data.orderSpecificPayment.sumDiscountPrice").exists())
                .andExpect(jsonPath("data.orderSpecificPayment.sumPrice").exists())
                .andExpect(jsonPath("data.orderSpecificPayment.deliveryFee").exists())
                .andExpect(jsonPath("data.orderSpecificPayment.sumPaymentPrice").exists())
                .andExpect(jsonPath("data.orderDestination").exists())
                .andExpect(jsonPath("data.orderDestination.recipient").exists())
                .andExpect(jsonPath("data.orderDestination.addressSimple").exists())
                .andExpect(jsonPath("data.orderDestination.addressDetail").exists())
                .andExpect(jsonPath("data.orderDestination.tel1").exists())
                .andExpect(jsonPath("data.orderDestination.zipcode").exists())
                .andExpect(jsonPath("data.orderDestination.message").exists())
                .andExpect(jsonPath("data.orderProducts").isArray())
                .andExpect(jsonPath("data.orderProducts[0].orderProductId").exists())
                .andExpect(jsonPath("data.orderProducts[0].productId").exists())
                .andExpect(jsonPath("data.orderProducts[0].name").exists())
                .andExpect(jsonPath("data.orderProducts[0].quantity").exists())
                .andExpect(jsonPath("data.orderProducts[0].price").exists())
                .andExpect(jsonPath("data.orderProducts[0].orderProductState").exists())
                .andExpect(jsonPath("data.orderProducts[0].thumbnail").exists())
                .andDo(document(
                        "query-order",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.orderSpecificCode").description("주문 상세 고유 코드 번호"),
                                fieldWithPath("data.orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상세 상태")),
                                fieldWithPath("data.orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 교환 상태")),
                                fieldWithPath("data.deliveryApiUrl").description("배송조회 API URL").type("Stirng").optional(),
                                fieldWithPath("data.orderSpecificPayment").description("주문 상세 금액 정보"),
                                fieldWithPath("data.orderSpecificPayment.sumOriginalPrice").description("소비자 가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.sumDiscountPrice").description("할인 가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.sumPrice").description("가격 총합"),
                                fieldWithPath("data.orderSpecificPayment.deliveryFee").description("배송비"),
                                fieldWithPath("data.orderSpecificPayment.sumPaymentPrice").description("실제 결제에 참여되는 금액 총합"),
                                fieldWithPath("data.orderSpecificPayment.paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD, "결제 수단")),
                                fieldWithPath("data.orderSpecificPayment.paymentDateTime").description("결제 시간").type("DateTime").optional(),
                                fieldWithPath("data.orderDestination").description("배송 정보"),
                                fieldWithPath("data.orderDestination.recipient").description("수령인"),
                                fieldWithPath("data.orderDestination.addressSimple").description("수령 주소 요약"),
                                fieldWithPath("data.orderDestination.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.orderDestination.tel1").description("전화번호 1"),
                                fieldWithPath("data.orderDestination.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.orderDestination.zipcode").description("우편번호"),
                                fieldWithPath("data.orderDestination.message").description("배송 메세지"),
                                fieldWithPath("data.orderProducts[]").description("주문 상품 리스트"),
                                fieldWithPath("data.orderProducts[].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderProducts[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.orderProducts[].name").description("상품명"),
                                fieldWithPath("data.orderProducts[].quantity").description("주문 수량"),
                                fieldWithPath("data.orderProducts[].price").description("결제에 참여한 가격"),
                                fieldWithPath("data.orderProducts[].orderProductState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE, "주문 상품 상태")),
                                fieldWithPath("data.orderProducts[].orderProductExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE, "주문 상품 교환 상태")),
                                fieldWithPath("data.orderProducts[].thumbnail").description("썸네일 이미지"),
                                fieldWithPath("data.orderProducts[].expiredDate").description("유통기한 만료일").optional().type("Date")
                        )

                ));
    }


    @Test
    @DisplayName("/api/orders/destination/{orderSpecificId} 주문 배송지 변경")
    void updateOrderDestination() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();

        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        orderSpecific.modifyOrderState(OrderState.PAYMENT_ACCEPT);
        OrderDestinationSaveDto save2 = OrderDestinationSaveDto.builder()
                .recipient("recipient1")
                .addressSimple("updateaddress12")
                .addressDetail("updateaddressDetail1")
                .tel1("01011111111")
                .tel2("01025222222")
                .zipcode("40153")
                .message("ismessage")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/destination/{orderSpecificId}", orderSpecificId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(save2))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-order-destination",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("message").description("배송 메세지").optional(),
                                fieldWithPath("recipient").description("수령인 이름").optional(),
                                fieldWithPath("tel1").description("전화번호 1").optional(),
                                fieldWithPath("tel2").description("전화번호 2"),
                                fieldWithPath("addressSimple").description("수령 주소").optional(),
                                fieldWithPath("addressDetail").description("수령 주소 상세").optional(),
                                fieldWithPath("zipcode").description("우편 번호").optional()
                        )
                ));


    }

    @Test
    @DisplayName("/api/orders/destination/{orderSpecificId} 주문 배송지 변경 실패")
    void updateOrderDestinationFailByState() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();
        order.paymentAccept();

        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        orderSpecific.modifyOrderState(OrderState.DELIVERY_WAIT);
        OrderDestinationSaveDto save2 = OrderDestinationSaveDto.builder()
                .recipient("recipient1")
                .addressSimple("updateaddress12")
                .addressDetail("updateaddressDetail1")
                .tel1("01011111111")
                .tel2("01025222222")
                .zipcode("40153")
                .message("ismessage")
                .build();

        int expectedStatus = 4028;
        String expectedMessage = "배송지를 변경할 수 없는 상태입니다. 배송지 변경은 결제완료 상태에서만 가능합니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/destination/{orderSpecificId}", orderSpecificId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(save2))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist());


    }


    @Test
    @DisplayName("/api/orders/accept/{orderSpecificId} 구매 완료 상태로 변경")
    void updateAcceptOrder() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();

        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        orderSpecific.modifyOrderState(OrderState.DELIVERY_ACCEPT);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            orderProduct.modifyOrderState(OrderState.DELIVERY_ACCEPT);
        }

        em.flush();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/accept/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-order-accept",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        )
                ));

        Assertions.assertEquals(OrderState.ORDER_ACCEPT, orderSpecific.getOrderState());
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            Assertions.assertEquals(OrderState.ORDER_ACCEPT, orderProduct.getOrderProductState());
        }

    }

    @Test
    @DisplayName("/api/orders/accept/{orderSpecificId} 구매 완료 변경 실패")
    void updateAcceptOrderFailByState() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();
        order.paymentAccept();

        int expectedStatus = 4029;
        String expectedMessage = "구매 확정으로 변경할 수 없는 상태입니다. 구매 확정은 배송완료 상태에서만 가능합니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/accept/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist());

    }


    @Test
    @DisplayName("/api/orders/exchange/{orderSpecificId} 상품 교환 요청")
    void orderExchangeRequest() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();

        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        Long orderSpecificId = orderSpecific.getId();
        em.flush();
        testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.DELIVERY_ACCEPT);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            testService.orderProductModifyState(orderProduct.getId(), OrderState.DELIVERY_ACCEPT);
        }

        List<OrderProductIdAndQuantityDto> productIdAndQuantityDtos = orderSpecific.getOrderProducts().stream().map(op -> OrderProductIdAndQuantityDto.builder()
                .orderProductId(op.getId())
                .quantity(Math.max(op.getQuantity() - 1, 1))
                .build()
        ).collect(Collectors.toList());


        OrderExchangeRequestDto orderExchangeRequestDto = OrderExchangeRequestDto.builder()
                .causeImage(Arrays.asList("/image001", "/image002", "/image003", "/image004"))
                .exchangeCause("cause message")
                .ordersProducts(productIdAndQuantityDtos)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/orders/exchange/{orderSpecificId}", orderSpecificId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderExchangeRequestDto))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-order-exchange",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("causeImage").description("교환 이미지 PATH LIST"),
                                fieldWithPath("exchangeCause").description("교환 사유 메시지"),
                                fieldWithPath("ordersProducts").description("교환 상품 목록"),
                                fieldWithPath("ordersProducts[].orderProductId").description("교환 주문 상품 고유 번호"),
                                fieldWithPath("ordersProducts[].quantity").description("교환 상품 개수")
                        )
                ));
    }


    @Test
    @DisplayName("/api/orders/exchange/{orderSpecificId} 상품 교환 조희")
    void queryOrderExchange() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();

        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        Long orderSpecificId = orderSpecific.getId();
        em.flush();
        testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.DELIVERY_ACCEPT);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            testService.orderProductModifyState(orderProduct.getId(), OrderState.DELIVERY_ACCEPT);
        }

        List<OrderProductIdAndQuantityDto> productIdAndQuantityDtos = orderSpecific.getOrderProducts().stream().map(op -> OrderProductIdAndQuantityDto.builder()
                .orderProductId(op.getId())
                .quantity(Math.max(op.getQuantity() - 1, 1))
                .build()
        ).collect(Collectors.toList());


        OrderExchangeRequestDto orderExchangeRequestDto = OrderExchangeRequestDto.builder()
                .causeImage(Arrays.asList("/image001", "/image002", "/image003", "/image004"))
                .exchangeCause("cause message")
                .ordersProducts(productIdAndQuantityDtos)
                .build();

        orderExchangeService.orderExchangeRequest(orderSpecificId, orderExchangeRequestDto, member);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/exchange/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("query-order-exchange",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.orderSpecificId").description("주문 상세 고유 번호"),
                                fieldWithPath("data.causeImage").description("이미지 PATH LIST"),
                                fieldWithPath("data.exchangeCause").description("교환 사유 메시지"),
                                fieldWithPath("data.orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE)),
                                fieldWithPath("data.orderProductExchanges").description("주문 상품 교환 리스트"),
                                fieldWithPath("data.orderProductExchanges[].orderProductId").description("주문 상품 고유 번호"),
                                fieldWithPath("data.orderProductExchanges[].productId").description("상품 고유 번호"),
                                fieldWithPath("data.orderProductExchanges[].thumbnail").description("썸네일 이미지 리스트"),
                                fieldWithPath("data.orderProductExchanges[].name").description("상품 이름"),
                                fieldWithPath("data.orderProductExchanges[].quantity").description("주문 개수"),
                                fieldWithPath("data.orderProductExchanges[].exchangeQuantity").description("교환 개수"),
                                fieldWithPath("data.orderProductExchanges[].orderProductExchangeState").description("주문 상품 교환 상태").type(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE))
                        )
                ));

    }


    @Test
    @DisplayName("/api/orders/exchange/{orderSpecificId} 상품 교환 조희")
    void cancelOrderExchange() throws Exception {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();

        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        OrderSpecific orderSpecific = order.getOrderSpecifics().get(0);
        Long orderSpecificId = orderSpecific.getId();

        em.flush();

        testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.DELIVERY_ACCEPT);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            testService.orderProductModifyState(orderProduct.getId(), OrderState.DELIVERY_ACCEPT);
        }

        List<OrderProductIdAndQuantityDto> productIdAndQuantityDtos = orderSpecific.getOrderProducts().stream().map(op -> OrderProductIdAndQuantityDto.builder()
                .orderProductId(op.getId())
                .quantity(Math.max(op.getQuantity() - 1, 1))
                .build()
        ).collect(Collectors.toList());


        OrderExchangeRequestDto orderExchangeRequestDto = OrderExchangeRequestDto.builder()
                .causeImage(Arrays.asList("/image001", "/image002", "/image003", "/image004"))
                .exchangeCause("cause message")
                .ordersProducts(productIdAndQuantityDtos)
                .build();

        orderExchangeService.orderExchangeRequest(orderSpecificId, orderExchangeRequestDto, member);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/orders/exchange/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-order-exchange",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        )
                ));

        Assertions.assertEquals(orderSpecific.getOrderExchangeState(), OrderExchangeState.NONE);
        Assertions.assertEquals(orderSpecific.getOrderExchange(), null);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            Assertions.assertEquals(orderProduct.getOrderProductExchangeState(), OrderExchangeState.NONE);
            Assertions.assertEquals(orderProduct.getOrderExchangeProduct(), null);
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
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();
    }

}
