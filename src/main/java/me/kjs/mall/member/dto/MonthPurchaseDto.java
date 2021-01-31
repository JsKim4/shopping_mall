package me.kjs.mall.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthPurchaseDto {
    int maximumPurchaseLimit;
    int orderPriceByMonth;
    int remainPrice;

    public static MonthPurchaseDto createMonthPurchaseDto(int maximumPurchaseLimit, int orderPriceByMonth) {
        return MonthPurchaseDto.builder()
                .maximumPurchaseLimit(maximumPurchaseLimit)
                .orderPriceByMonth(orderPriceByMonth)
                .remainPrice(maximumPurchaseLimit - orderPriceByMonth)
                .build();
    }
}
