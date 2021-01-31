package me.kjs.mall.product.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountSchedule {
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private int discountAmount;

    public DiscountType getDiscountType() {
        return discountType;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean isNowApply() {
        if (beginDate.isAfter(LocalDateTime.now())) {
            return false;
        }
        if (endDate.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public static DiscountSchedule initDefault(DiscountType discountType, int discountAmount) {
        return DiscountSchedule.builder()
                .discountAmount(discountAmount)
                .discountType(discountType)
                .build();
    }

    public void loading() {
        getDiscountType();
    }
}

