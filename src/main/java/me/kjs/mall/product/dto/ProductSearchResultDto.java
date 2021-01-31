package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResultDto {
    private String keyword;
    @Builder.Default
    private List<ProductDetailDto> products = new ArrayList<>();

    public static ProductSearchResultDto productsToSearchResultDto(List<ProductDetailDto> products, ProductSearchCondition productSearchCondition) {
        return ProductSearchResultDto.builder()
                .keyword(productSearchCondition.getKeyword())
                .products(products)
                .build();
    }
}
