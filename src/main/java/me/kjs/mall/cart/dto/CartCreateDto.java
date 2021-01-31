package me.kjs.mall.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartCreateDto {
    private Long productId;
    @Min(1)
    @Max(100)
    @Builder.Default
    private int quantity = 1;
}
