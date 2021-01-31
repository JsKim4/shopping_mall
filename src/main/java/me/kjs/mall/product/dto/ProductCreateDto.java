package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.type.DiscountType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
    @NotNull
    private Long baseProductId;
    @Min(0)
    private int stock;
    @NotNull
    private LocalDateTime salesBeginDate;
    @NotNull
    private LocalDateTime salesEndDate;
    @NotNull
    private DiscountType discountType = DiscountType.NONE;
    private int discountAmount;
}
