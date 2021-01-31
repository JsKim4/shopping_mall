package me.kjs.mall.partial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.partial.NewProduct;
import me.kjs.mall.product.dto.ProductSimpleDto;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDto {
    private ProductSimpleDto productInfo;
    private LocalDate beginDate;
    private LocalDate endDate;

    public static NewProductDto newProductToDto(NewProduct newProduct) {
        return NewProductDto.builder()
                .beginDate(newProduct.getBeginDate())
                .endDate(newProduct.getEndDate())
                .productInfo(ProductSimpleDto.productToSimpleDto(newProduct.getProduct()))
                .build();
    }
}
