package me.kjs.mall.api.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.order.OrderService;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.destination.Carrier;
import me.kjs.mall.order.specific.destination.dto.OrderDeliveryDoingDto;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Profile({"default", "test-server", "local-mysql", "anything", "staging-server"})
@RequestMapping("/test/api")
@Slf4j
public class TestApiController {

    private final TestService testService;

    private final OrderService orderService;

    @RequestMapping("/orders/{orderSpecificId}/{orderState}")
    public ResponseDto orderSpecificModifyState(@PathVariable("orderSpecificId") Long orderSpecificId,
                                                @PathVariable("orderState") OrderState orderState) {
        testService.orderSpecificModifyState(orderSpecificId, orderState);
        return ResponseDto.noContent();
    }

    @RequestMapping("/orders/products/{orderProductId}/{orderState}")
    public ResponseDto orderSpecificProductModifyState(@PathVariable("orderProductId") Long orderProductId,
                                                       @PathVariable("orderState") OrderState orderState) {
        testService.orderProductModifyState(orderProductId, orderState);
        return ResponseDto.noContent();
    }


    @RequestMapping("/orders/exchange/{orderSpecificId}/{orderExchangeState}")
    public ResponseDto orderSpecificModifyExchangeState(@PathVariable("orderSpecificId") Long orderSpecificId,
                                                        @PathVariable("orderExchangeState") OrderExchangeState orderState) {
        testService.orderSpecificModifyExchangeState(orderSpecificId, orderState);
        return ResponseDto.noContent();
    }

    @RequestMapping("/orders/products/exchange/{orderProductId}/{orderExchangeState}")
    public ResponseDto orderSpecificProductModifyExchangeState(@PathVariable("orderProductId") Long orderProductId,
                                                               @PathVariable("orderExchangeState") OrderExchangeState orderState) {
        testService.orderProductModifyExchangeState(orderProductId, orderState);
        return ResponseDto.noContent();
    }


    @RequestMapping("/orders/delivery/doing/{orderSpecificId}")
    public ResponseDto orderSpecificDeliveryDoing(@PathVariable("orderSpecificId") Long orderSpecificId) {

        orderService.deliveryDoingOrder(orderSpecificId, OrderDeliveryDoingDto.builder()
                .carrier(Carrier.CJ)
                .invoiceNumber("1111111111")
                .build());
        return ResponseDto.noContent();
    }

    @RequestMapping("/orders/delivery/accept/{orderSpecificId}")
    public ResponseDto orderSpecificAcceptDoing(@PathVariable("orderSpecificId") Long orderSpecificId) {
        orderService.deliveryAcceptOrder(orderSpecificId);
        return ResponseDto.noContent();
    }
}
