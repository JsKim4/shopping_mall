package me.kjs.mall.product.type;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.dto.ProductDeliveryDto;

import javax.persistence.*;

@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ProductDelivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_delivery_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private YnType deliveryYn;
    @Enumerated(EnumType.STRING)
    private YnType bundleYn;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    private String returnLocation;
    private int fee;
    private int feeCondition;

    public static ProductDelivery createProductDelivery(ProductDeliveryDto productDelivery) {
        return ProductDelivery.builder()
                .deliveryYn(productDelivery.getDeliveryYn())
                .bundleYn(productDelivery.getBundleYn())
                .deliveryType(productDelivery.getDeliveryType())
                .returnLocation(productDelivery.getReturnLocation())
                .fee(productDelivery.getFee())
                .feeCondition(productDelivery.getFeeCondition())
                .build();
    }

    public int calculateDeliveryFee(int price) {
        return deliveryType.getFee(this, price);
    }

    public void update(ProductDeliveryDto productDelivery) {
        deliveryType = productDelivery.getDeliveryType();
        bundleYn = productDelivery.getBundleYn();
        deliveryYn = productDelivery.getDeliveryYn();
        returnLocation = productDelivery.getReturnLocation();
        fee = productDelivery.getFee();
        feeCondition = productDelivery.getFeeCondition();
    }
}
