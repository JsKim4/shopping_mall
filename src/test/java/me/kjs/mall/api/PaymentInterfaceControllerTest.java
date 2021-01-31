package me.kjs.mall.api;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentInterfaceControllerTest extends BaseTest {

    @Test
    void getPaymentFailTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/payment"))
                .andExpect(status().isOk())
                .andDo(document(
                        "interface-payment",
                        responseFields(
                                fieldWithPath("status").description("결제 결과코드 200제외 모두 실패"),
                                fieldWithPath("message").description("결제 결과 메세지"),
                                fieldWithPath("data.orderId").description("주문 번호"),
                                fieldWithPath("data.orderSpecificId").description("주문 상세번호 리스트"),
                                fieldWithPath("data.orderCodes").description("주문 코드 리스트"),
                                fieldWithPath("data.orderPaymentDetail").description("결제 상세 정보"),
                                fieldWithPath("data.orderPaymentDetail.paymentPrice").description("결제 가격"),
                                fieldWithPath("data.orderPaymentDetail.paymentMethod").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PAYMENT_METHOD)),
                                fieldWithPath("data.orderPaymentDetail.paymentDateTime").description("결제 일시").type("date time"),
                                fieldWithPath("data.virtualBankResult").description("가상계좌 정보").optional(),
                                fieldWithPath("data.virtualBankResult.bankCode").description("은행 코드").optional(),
                                fieldWithPath("data.virtualBankResult.bankName").description("은행명").optional(),
                                fieldWithPath("data.virtualBankResult.bankNum").description("계좌번호").optional(),
                                fieldWithPath("data.virtualBankResult.bankExpiredDateTime").description("가상계좌 만료 시간").type("date time").optional()
                        )
                ));
    }

    @Test
    void getPaymentCreateOrderFormTest() throws Exception {
        PaymentInterfaceController.CreateOrderFormDto createOrderFormDto = new PaymentInterfaceController.CreateOrderFormDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/test/payment/orderForm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderFormDto)))
                .andExpect(status().isOk())
                .andDo(document(
                        "interface-payment-create-form",
                        requestFields(
                                fieldWithPath("goodsName").description("상품 이름"),
                                fieldWithPath("amt").description("결제 가격"),
                                fieldWithPath("payMethod").description("결제 종류"),
                                fieldWithPath("signData").description("암호화 데이터"),
                                fieldWithPath("mid").description("상점 아이디"),
                                fieldWithPath("ediDate").description("전문 생성 일시"),
                                fieldWithPath("moid").description("결제 코드"),
                                fieldWithPath("returnUrl").description("응답 URL"),
                                fieldWithPath("certificationRequestUrl").description("결제 생성 요청 URL"),
                                fieldWithPath("vbankExpDate").description("yyyyMMDDHHmm 가상계좌일 경우 넘김")
                        )
                ));
    }
}
