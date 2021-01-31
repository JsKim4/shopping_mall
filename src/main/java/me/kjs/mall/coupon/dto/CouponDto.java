package me.kjs.mall.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.coupon.Coupon;
import me.kjs.mall.product.type.DiscountType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private Long couponId;
    private String title;
    private String content;
    private DiscountType discountType;
    private int discountAmount;
    private int maxDiscountPrice;
    private int minPrice;
    private CommonStatus status;
    private int maxPeriod;

    public static CouponDto couponToDto(Coupon coupon) {
        return CouponDto.builder()
                .couponId(coupon.getId())
                .title(coupon.getTitle())
                .content(coupon.getContent())
                .discountType(coupon.getDiscountType())
                .discountAmount(coupon.getDiscountAmount())
                .maxDiscountPrice(coupon.getMaxDiscountPrice())
                .minPrice(coupon.getMinPrice())
                .status(coupon.getStatus())
                .maxPeriod(coupon.getMaxPeriod())
                .build();
    }

}
