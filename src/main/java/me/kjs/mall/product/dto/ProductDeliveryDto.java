package me.kjs.mall.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.type.DeliveryType;
import me.kjs.mall.product.type.ProductDelivery;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeliveryDto {
    private YnType deliveryYn = YnType.Y;
    private YnType bundleYn = YnType.Y;
    @NotNull
    private DeliveryType deliveryType;
    private String returnLocation;
    @Min(0)
    private int fee;
    @Min(0)
    private int feeCondition;

    public static ProductDeliveryDto productDeliveryToDto(ProductDelivery productDelivery) {
        return ProductDeliveryDto.builder()
                .deliveryYn(productDelivery.getDeliveryYn())
                .bundleYn(productDelivery.getBundleYn())
                .deliveryType(productDelivery.getDeliveryType())
                .returnLocation(productDelivery.getReturnLocation())
                .fee(productDelivery.getFee())
                .feeCondition(productDelivery.getFeeCondition())
                .build();
    }
}
