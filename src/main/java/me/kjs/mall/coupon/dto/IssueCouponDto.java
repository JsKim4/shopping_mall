package me.kjs.mall.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.coupon.IssueCoupon;
import me.kjs.mall.product.type.DiscountType;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCouponDto {
    private String title;
    private String content;
    private LocalDate expiredDate;
    private int remainUsePeriod;
    private DiscountType discountType;
    private int discountAmount;
    private int maxDiscountPrice;
    private int minPrice;

    public static IssueCouponDto issueCouponToDto(IssueCoupon issueCoupon) {
        return IssueCouponDto.builder()
                .title(issueCoupon.getTitle())
                .content(issueCoupon.getContent())
                .expiredDate(issueCoupon.getExpiredDate())
                .remainUsePeriod(issueCoupon.getRemainUsePeriod())
                .discountType(issueCoupon.getDiscountType())
                .discountAmount(issueCoupon.getDiscountAmount())
                .maxDiscountPrice(issueCoupon.getMaxDiscountPrice())
                .minPrice(issueCoupon.getMinPrice())
                .build();
    }
}
