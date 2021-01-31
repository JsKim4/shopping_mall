package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockModifyDto {
    @Min(0)
    private int modifierStock;

    public static ProductStockModifyDto adminModifyStock(int stock) {
        return ProductStockModifyDto.builder()
                .modifierStock(stock)
                .build();
    }

    public static ProductStockModifyDto paymentApproveFail(int stock) {
        return ProductStockModifyDto.builder()
                .modifierStock(stock)
                .build();
    }

    public static ProductStockModifyDto paymentApproveRequest(int stock) {
        return ProductStockModifyDto.builder()
                .modifierStock(stock)
                .build();
    }

    public static ProductStockModifyDto orderCancel(int stock) {
        return ProductStockModifyDto.builder()
                .modifierStock(stock)
                .build();
    }
}
