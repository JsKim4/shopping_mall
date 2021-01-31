package me.kjs.mall.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.order.dto.OrderProductDetailOnAdminDto;
import me.kjs.mall.order.dto.PlaceOrderDownloadForErpItemFailDto;
import me.kjs.mall.order.dto.PlaceOrderDownloadForErpItemSuccessDto;
import me.kjs.mall.order.dto.PlaceOrderDownloadForErpResultDto;
import me.kjs.mall.order.exception.NotAvailablePlaceOrderProductException;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.order.specific.product.OrderProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderAdminService {
    private final OrderProductRepository orderProductRepository;
    private final OrderSpecificRepository orderSpecificRepository;
    private final PlaceOrderLogRepository placeOrderLogRepository;

    public OrderProductDetailOnAdminDto findOrderProductDetailOnAdminDto(Long orderProductId) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(NoExistIdException::new);
        return OrderProductDetailOnAdminDto.orderProductToDetailOnDto(orderProduct);
    }

    public PlaceOrderDownloadForErpResultDto findPlaceOrderForErp(OnlyIdsDto onlyIds) {
        List<PlaceOrderDownloadForErpItemSuccessDto> successes = new ArrayList<>();
        List<PlaceOrderDownloadForErpItemFailDto> failures = new ArrayList<>();
        List<OrderSpecific> orderSpecifics = orderSpecificRepository.findAllById(onlyIds.getIds());
        for (OrderSpecific orderSpecific : orderSpecifics) {
            boolean isFailures = false;
            String failureCause = "";
            List<PlaceOrderDownloadForErpItemSuccessDto> tempSuccesses = new ArrayList<>();
            for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                if (!orderProduct.isAvailablePlaceOrder()) {
                    isFailures = true;
                    failureCause = orderProduct.getOrderItemCode() + " 주문 상태값이 " + orderProduct.getOrderProductState().getDescription() + "입니다.";
                    break;
                }
                PlaceOrderDownloadForErpItemSuccessDto placeOrderDownloadForErpItemSuccessDto = PlaceOrderDownloadForErpItemSuccessDto.orderProductToPlaceOrderDownloadForErpDto(orderProduct, orderSpecific.getOrderDestination());
                tempSuccesses.add(placeOrderDownloadForErpItemSuccessDto);
            }
            if (isFailures) {
                for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
                    PlaceOrderDownloadForErpItemFailDto placeOrderDownloadForErpItemFailDto =
                            PlaceOrderDownloadForErpItemFailDto.
                                    orderProductToPlaceOrderDownloadForErpFailDto(orderProduct, orderSpecific.getOrderDestination(), failureCause);
                    failures.add(placeOrderDownloadForErpItemFailDto);
                }
                continue;
            }
            successes.addAll(tempSuccesses);

        }
        return PlaceOrderDownloadForErpResultDto.createPlaceOrderDownloadForErpResultDto(successes, failures);
    }

    @Transactional
    public void createPlaceOrderLog(OnlyIdsDto onlyIds) {
        PlaceOrderLog placeOrderLog = PlaceOrderLog.createLog(onlyIds.getIds().toString());
        placeOrderLogRepository.save(placeOrderLog);
    }

    @Transactional
    public void placeAccept(Long orderSpecificId) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(orderSpecific);
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            if (!orderProduct.isAvailablePlaceOrder()) {
                throw new NotAvailablePlaceOrderProductException(orderProduct.getOrderItemCode(), "주문 상태값이 발주 가능한 상태가 아닙니다. 다시한번 확인해주세요.");
            }
        }
        orderSpecific.placeAccept();
    }
}
