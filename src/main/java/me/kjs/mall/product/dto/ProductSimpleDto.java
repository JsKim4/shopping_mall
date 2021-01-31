package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.Product;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSimpleDto {
    private Long productId;
    private String name;
    private List<String> thumbnailImage;
    private int originPrice;
    private int stock;
    private CommonStatus commonStatus;
    private LocalDateTime salesBeginDate;
    private LocalDateTime salesEndDate;
    private int discountPrice;
    private int discountPercent;
    private int price;

    public static ProductSimpleDto productToSimpleDto(Product product) {
        return ProductSimpleDto.builder()
                .productId(product.getId())
                .name(product.getBaseProductName())
                .thumbnailImage(product.getBaseProductThumbnailImage())
                .originPrice(product.getOriginPrice())
                .stock(product.getStock())
                .commonStatus(product.getStatus())
                .salesBeginDate(product.getSalesBeginDate())
                .salesEndDate(product.getSalesEndDate())
                .discountPrice(product.getDiscountPrice())
                .discountPercent(product.getDiscountPercent())
                .price(product.getPrice())
                .build();
    }
}
