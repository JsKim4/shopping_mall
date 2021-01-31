package me.kjs.mall.order.specific.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.point.Point;
import me.kjs.mall.product.type.DiscountType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductPayment {
    private int quantity;
    private int originPrice;
    private int discountPrice;
    private int price;
    private int remainQuantity;
    private int sumOriginPrice;
    private int sumDiscountPrice;
    private int sumPrice;
    private int accumulateExpectedPoint;
    private int accumulatePoint;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public static OrderProductPayment createOrderProductPayment(ProductAndQuantityDto productDto, int accumulateExpectedPoint) {
        return OrderProductPayment.builder()
                .quantity(productDto.getQuantity())
                .originPrice(productDto.getOriginPrice())
                .discountPrice(productDto.getDiscountPrice())
                .price(productDto.getPrice())
                .remainQuantity(productDto.getQuantity())
                .sumOriginPrice(productDto.getSumOriginPrice())
                .sumDiscountPrice(productDto.getSumDiscountPrice())
                .sumPrice(productDto.getSumPrice())
                .accumulateExpectedPoint(accumulateExpectedPoint)
                .build();
    }


    public boolean isRemainQuantityLessThen(int quantity) {
        return remainQuantity < quantity;
    }

    public int getSumOriginPrice() {
        return sumOriginPrice;
    }

    public int getSumDiscountPrice() {
        return sumDiscountPrice;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAccumulateExpectedPoint() {
        return accumulateExpectedPoint;
    }

    public void setAccumulatePoint(Point point) {
        accumulatePoint = point.getAmount();
    }
}
