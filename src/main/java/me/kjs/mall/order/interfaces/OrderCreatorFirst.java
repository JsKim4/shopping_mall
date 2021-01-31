package me.kjs.mall.order.interfaces;

import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;

import java.util.List;

public interface OrderCreatorFirst {
    List<Long> getProductIds();

    List<ProductIdAndQuantityDto> getProducts();

    int getPoint();

    PaymentMethod getPaymentMethod();

    List<OrderDestinationSaveDto> getDestination();
}
