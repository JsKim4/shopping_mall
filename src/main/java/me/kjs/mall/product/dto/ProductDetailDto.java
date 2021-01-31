package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.base.dto.BaseProductDetailDto;
import me.kjs.mall.product.type.DiscountType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private Long productId;
    private int originPrice;
    private int discountPrice;
    private int price;
    private int discountPercent;
    private DiscountType discountType;
    private CommonStatus productStatus;
    private LocalDateTime salesBeginDate;
    private LocalDateTime salesEndDate;
    private int orderCount;
    private int stock;
    private BaseProductDetailDto baseProduct;
    private YnType wishYn;
    private EvaluationDto evaluation;


    public static ProductDetailDto productToDetailDto(Product product) {
        ProductDetailDto productDetailDto = getProductDetailDto(product);
        productDetailDto.wishYn = YnType.N;
        return productDetailDto;
    }

    public static ProductDetailDto productToDetailDto(Product product, YnType wishYn) {
        ProductDetailDto productDetailDto = getProductDetailDto(product);
        productDetailDto.wishYn = wishYn;
        return productDetailDto;
    }

    private static ProductDetailDto getProductDetailDto(Product product) {
        return ProductDetailDto.builder()
                .baseProduct(BaseProductDetailDto.baseProductToDetailDto(product.getBaseProduct()))
                .productId(product.getId())
                .originPrice(product.getOriginPrice())
                .discountPrice(product.getDiscountPrice())
                .price(product.getPrice())
                .stock(product.getStock())
                .discountPercent(product.getDiscountPercent())
                .discountType(product.getDiscountType())
                .productStatus(product.getStatus())
                .salesBeginDate(product.getSalesBeginDate())
                .salesEndDate(product.getSalesEndDate())
                .evaluation(EvaluationDto.evaluationToDto(product.getEvaluation()))
                .build();
    }
}
