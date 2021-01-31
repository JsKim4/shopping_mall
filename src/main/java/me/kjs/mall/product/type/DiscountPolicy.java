package me.kjs.mall.product.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductUpdateDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicy {
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private int discountAmount;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<DiscountSchedule> discountSchedules = new ArrayList<>();

    public static DiscountPolicy createDiscountPolicy(ProductCreateDto productCreateDto) {
        return DiscountPolicy.builder()
                .discountAmount(productCreateDto.getDiscountAmount())
                .discountType(productCreateDto.getDiscountType())
                .build();
    }

    public DiscountType getDiscountType() {
        return discountSchedules.stream().filter(DiscountSchedule::isNowApply).findFirst().orElse(DiscountSchedule.initDefault(discountType, discountAmount)).getDiscountType();
    }

    public int getDiscountPrice(int originPrice) {
        DiscountType discountType = getDiscountType();
        if (discountType == DiscountType.FLAT_RATE) {
            return discountAmount;
        } else if (discountType == DiscountType.PERCENT) {
            return originPrice / 100 * discountAmount;
        } else {
            return 0;
        }
    }

    public void updateProduct(ProductUpdateDto productUpdateDto) {
        if (productUpdateDto.getDiscountType() != null) {
            discountType = productUpdateDto.getDiscountType();
            discountAmount = productUpdateDto.getDiscountAmount();
        }
    }

    public int getDiscountPercent(int originPrice) {
        DiscountType discountType = getDiscountType();
        if (discountType == DiscountType.PERCENT)
            return discountAmount;
        else if (discountType == DiscountType.FLAT_RATE) {
            return discountAmount * 100 / originPrice;
        }
        return 0;
    }

    public void loading() {
        for (DiscountSchedule discountSchedule : discountSchedules) {
            discountSchedule.loading();
        }
    }
}
