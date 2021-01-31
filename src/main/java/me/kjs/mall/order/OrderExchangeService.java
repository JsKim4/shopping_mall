package me.kjs.mall.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CompareUtil;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.order.dto.OrderProductAndQuantityDto;
import me.kjs.mall.order.exception.NotAvailableExchangeCancelException;
import me.kjs.mall.order.exception.NotAvailableExchangeRequestException;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.order.specific.product.dto.OrderExchangeRequestDto;
import me.kjs.mall.order.specific.product.dto.OrderProductIdAndQuantityDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderExchangeService {

    private final OrderSpecificRepository orderSpecificRepository;
    private final OrderProductRepository orderProductRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrderSpecific orderExchangeRequest(Long orderSpecificId, OrderExchangeRequestDto orderExchangeRequestDto, Member member) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        Member saveMember = memberRepository.save(member);
        ThrowUtil.notOwnerThrow(orderSpecific, saveMember);
        if (!orderSpecific.isExchangeRequestAvailable()) {
            throw new NotAvailableExchangeRequestException();
        }
        List<OrderProductIdAndQuantityDto> ordersProductIdAndQuantities = orderExchangeRequestDto.getOrdersProducts();
        List<Long> orderProductIds = ordersProductIdAndQuantities.stream().map(OrderProductIdAndQuantityDto::getOrderProductId).collect(Collectors.toList());
        List<OrderProduct> orderProducts = orderProductRepository.findAllById(orderProductIds);

        if (orderProductIds.size() != orderProducts.size()) {
            throw new NoExistIdException();
        }

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getOrderSpecific() != orderSpecific) {
                throw new BadRequestException(ExceptionStatus.RUN_TIME_EXCEPTION, "상품 상세와 주문 상품의 상세가 서로 다름");
            }
        }

        List<OrderProductAndQuantityDto> exchangeProductList = orderProductsToProductAndQuantityList(orderProducts, ordersProductIdAndQuantities);
        for (OrderProductAndQuantityDto orderProductAndQuantityDto : exchangeProductList) {
            if (!orderProductAndQuantityDto.getOrderProduct().isAvailableExchangeRequest(orderProductAndQuantityDto.getQuantity())) {
                throw new NotAvailableExchangeRequestException();
            }
        }
        return orderSpecific.requestExchange(exchangeProductList, orderExchangeRequestDto).loading();
    }


    private List<OrderProductAndQuantityDto> orderProductsToProductAndQuantityList(List<OrderProduct> orderProducts, List<OrderProductIdAndQuantityDto> orderProductIdAndQuantityDtos) {
        final List<OrderProductAndQuantityDto> orderProductAndQuantityDtos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductIdAndQuantityDto orderproductIdAndQuantityDto = orderProductIdAndQuantityDtos.stream()
                    .filter(orderProductId -> CompareUtil.equals(orderProductId.getOrderProductId(), orderProduct.getId()))
                    .findFirst().orElseThrow(NoExistIdException::new);
            orderProductAndQuantityDtos.add(new OrderProductAndQuantityDto(orderProduct, orderproductIdAndQuantityDto.getQuantity()));
        }
        return orderProductAndQuantityDtos;
    }

    @Transactional
    public void orderExchangeCancel(Long orderSpecificId, Member member) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        Member saveMember = memberRepository.save(member);
        ThrowUtil.notOwnerThrow(orderSpecific, saveMember);
        if (!orderSpecific.isExchangeCancelAvailable()) {
            throw new NotAvailableExchangeCancelException();
        }

        orderSpecific.cancelExchange();
    }
}
