package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.type.DiscountType;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {
    private LocalDateTime salesBeginDate;
    private LocalDateTime salesEndDate;
    private DiscountType discountType;
    @Min(0)
    private int discountAmount;
}
