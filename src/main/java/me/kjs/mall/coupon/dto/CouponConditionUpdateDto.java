package me.kjs.mall.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponConditionUpdateDto {
    private boolean useDateTime;
    private LocalDateTime beginDateTime;
    private LocalDateTime endDateTime;
}
