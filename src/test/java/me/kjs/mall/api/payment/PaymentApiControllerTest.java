package me.kjs.mall.api.payment;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.Bank;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.cancel.dto.CancelCauseNonMemberDto;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderCreateByNonMemberDto;
import me.kjs.mall.order.dto.OrderNonMemberInfoDto;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentApiControllerTest extends BaseTest {

    @Test
    void orderCancelTest() throws Exception {

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
                                .build()
                ))
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());

        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/payment/cancel/{orderSpecificId}", orderSpecificId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content("{\"cause\":\"message\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "update-order-cancel",
                        requestFields(
                                fieldWithPath("cause").description("취소 사유")
                        ),
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 번호")
                        )
                ));
    }


    @Test
    void orderCancelNonTest() throws Exception {

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
                                .build()
                ))
                .memberInfo(OrderNonMemberInfoDto.builder()
                        .email("email001@naver.com")
                        .name("honghong")
                        .phoneNumber("01026420239")
                        .build())
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();


        Order order = orderService.createOrder(orderCreateDto);

        Long orderSpecificId = order.getOrderSpecifics().get(0).getId();

        CancelCauseNonMemberDto cancelCauseNonMemberDto = CancelCauseNonMemberDto.builder()
                .accountNo("01830104182923")
                .bank(Bank.KB)
                .cause("is test")
                .name("honghong")
                .phoneNumber("01026420239")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/payment/cancel/non/{orderSpecificId}", orderSpecificId)
                .content(objectMapper.writeValueAsString(cancelCauseNonMemberDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(
                        "update-order-cancel-non-member",
                        requestFields(
                                fieldWithPath("accountNo").description("가상계좌 결제 / 환불계좌").optional(),
                                fieldWithPath("bank").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.BANK, "가상계좌 결제")).optional(),
                                fieldWithPath("cause").description("취소 사유"),
                                fieldWithPath("name").description("구매시 입력한 이름"),
                                fieldWithPath("phoneNumber").description("구매시 입력한 전화번호")
                        ),
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 번호")
                        )
                ));
    }


}