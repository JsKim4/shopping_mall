package me.kjs.mall.order.specific.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductIdAndQuantityDto {
    @NotNull
    private Long orderProductId;
    @Min(1)
    private int quantity;
}
