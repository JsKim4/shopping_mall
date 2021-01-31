package me.kjs.mall.order.sheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetProductDto {
    private Long productId;
    private int quantity;
    private int requestQuantity;
    private List<String> thumbnail;
    private String name;
    private int discountPercent;
    private int originPrice;
    private int price;
    private int discountPrice;
    private int sumOriginPrice;
    private int sumPrice;
    private int sumDiscountPrice;
    private int stock;

    public static OrderSheetProductDto productAndQuantityToOrderSheetProductDto(ProductAndQuantityDto productAndQuantityDto) {
        return OrderSheetProductDto.builder()
                .productId(productAndQuantityDto.getProductId())
                .quantity(productAndQuantityDto.getQuantity())
                .requestQuantity(productAndQuantityDto.getRequestQuantity())
                .thumbnail(productAndQuantityDto.getThumbnail())
                .name(productAndQuantityDto.getName())
                .discountPercent(productAndQuantityDto.getDiscountPercent())
                .originPrice(productAndQuantityDto.getOriginPrice())
                .price(productAndQuantityDto.getPrice())
                .sumOriginPrice(productAndQuantityDto.getSumOriginPrice())
                .sumPrice(productAndQuantityDto.getSumPrice())
                .sumDiscountPrice(productAndQuantityDto.getSumDiscountPrice())
                .discountPrice(productAndQuantityDto.getDiscountPrice())
                .stock(productAndQuantityDto.getStock())
                .build();
    }
}