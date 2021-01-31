package me.kjs.mall.api.test;

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
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.Carrier;
import me.kjs.mall.order.specific.destination.dto.OrderDeliveryDoingDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Profile({"default", "test-server", "anything", "staging-server", "local-mysql"})
class TestApiControllerTest extends BaseTest {

    @Autowired
    private TestService testService;

    @BeforeEach
    void setUp() {
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
        for (int i = 0; i < 4; i++) {
            Order order = orderService.createOrder(orderCreateDto, member.getId());
        }

    }


    @Test
    void orderSpecificStateModify() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/{orderSpecificId}/{orderState}", orderSpecific.getId(), OrderState.PAYMENT_ACCEPT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-specific-state",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호"),
                                parameterWithName("orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE))
                        )
                ));
        Assertions.assertEquals(orderSpecific.getOrderState(), OrderState.PAYMENT_ACCEPT);
    }

    @Test
    void orderProductStateModify() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        OrderProduct orderProduct = orderSpecific.getOrderProducts().get(0);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/products/{orderProductId}/{orderState}", orderProduct.getId(), OrderState.PAYMENT_ACCEPT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-product-state",
                        pathParameters(
                                parameterWithName("orderProductId").description("주문 상세 고유 번호"),
                                parameterWithName("orderState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_STATE))
                        )
                ));
        Assertions.assertEquals(orderProduct.getOrderProductState(), OrderState.PAYMENT_ACCEPT);
    }


    @Test
    void orderSpecificExchangeStateModify() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/exchange/{orderSpecificId}/{orderExchangeState}", orderSpecific.getId(), OrderExchangeState.EXCHANGE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-specific-exchange-state",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호"),
                                parameterWithName("orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE))
                        )
                ));
        Assertions.assertEquals(orderSpecific.getOrderExchangeState(), OrderExchangeState.EXCHANGE_REQUEST);
    }

    @Test
    void orderProductExchangeStateModify() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        OrderProduct orderProduct = orderSpecific.getOrderProducts().get(0);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/products/exchange/{orderProductId}/{orderExchangeState}", orderProduct.getId(), OrderExchangeState.EXCHANGE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-product-exchange-state",
                        pathParameters(
                                parameterWithName("orderProductId").description("주문 상세 고유 번호"),
                                parameterWithName("orderExchangeState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ORDER_EXCHANGE_STATE))
                        )
                ));
        Assertions.assertEquals(orderProduct.getOrderProductExchangeState(), OrderExchangeState.EXCHANGE_REQUEST);
    }

    @Test
    void orderDeliveryDoingProduct() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.PAYMENT_ACCEPT);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/delivery/doing/{orderSpecificId}", orderSpecific.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-specific-delivery-doing",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        )
                ));
        Assertions.assertEquals(orderSpecific.getOrderState(), OrderState.DELIVERY_DOING);
        assertNotNull(orderSpecific.getDeliveryApiUrl());
    }


    @Test
    void orderDeliveryAcceptProduct() throws Exception {
        OrderSpecific orderSpecific = orderSpecificRepository.findAll().get(0);
        testService.orderSpecificModifyState(orderSpecific.getId(), OrderState.PAYMENT_ACCEPT);
        orderService.deliveryDoingOrder(orderSpecific.getId(), OrderDeliveryDoingDto.builder()
                .invoiceNumber("1111111111111")
                .carrier(Carrier.CJ)
                .build());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/api/orders/delivery/accept/{orderSpecificId}", orderSpecific.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "test-update-order-specific-delivery-accept",
                        pathParameters(
                                parameterWithName("orderSpecificId").description("주문 상세 고유 번호")
                        )
                ));
        Assertions.assertEquals(orderSpecific.getOrderState(), OrderState.DELIVERY_ACCEPT);
        assertNotNull(orderSpecific.getDeliveryApiUrl());
    }
}