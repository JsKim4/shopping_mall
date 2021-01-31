package me.kjs.mall.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.coupon.CouponStatus;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueCouponSearchCondition {
    @NotNull
    private CouponStatus couponStatus = CouponStatus.ISSUE;
}
