package me.kjs.mall.api.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.order.OrderProductRepository;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Profile({"default", "test-server", "anything", "local-mysql", "staging-server"})
public class TestService {

    private final OrderSpecificRepository orderSpecificRepository;

    private final OrderProductRepository orderProductRepository;

    @Transactional
    public void orderSpecificModifyState(Long orderSpecificId, OrderState orderState) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        orderSpecific.modifyOrderState(orderState);
    }

    @Transactional
    public void orderProductModifyState(Long orderProductId, OrderState orderState) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(NoExistIdException::new);
        orderProduct.modifyOrderState(orderState);

    }

    public void orderSpecificModifyExchangeState(Long orderSpecificId, OrderExchangeState orderState) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        orderSpecific.modifyOrderExchangeState(orderState);
    }

    public void orderProductModifyExchangeState(Long orderProductId, OrderExchangeState orderState) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(NoExistIdException::new);
        orderProduct.modifyOrderExchangeState(orderState);

    }
}
