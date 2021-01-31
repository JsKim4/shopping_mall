package me.kjs.mall.order.dto.create;

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
public class ProductIdAndQuantityDto {
    @NotNull
    private Long productId;
    @Min(1)
    private int quantity = 1;
}
