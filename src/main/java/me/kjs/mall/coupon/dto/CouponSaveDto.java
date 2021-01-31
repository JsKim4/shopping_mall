package me.kjs.mall.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.type.DiscountType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponSaveDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    private DiscountType discountType;
    @Min(1)
    private int discountAmount;
    @Min(0)
    private int maxDiscountPrice;
    @Min(0)
    private int minPrice;
    @Min(1)
    @Max(365)
    private int maxPeriod;

}
