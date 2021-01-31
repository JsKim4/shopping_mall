package me.kjs.mall.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.wish.Wish;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishProductDto {

    private Long productId;
    private String name;
    private int discountPercent;
    private int originPrice;
    private int price;
    private List<String> thumbnail;


    public static WishProductDto wishToWishProductDto(Wish wish) {
        return WishProductDto.builder()
                .productId(wish.getProductId())
                .name(wish.getProductName())
                .discountPercent(wish.getDiscountPercent())
                .originPrice(wish.getOriginPrice())
                .price(wish.getPrice())
                .thumbnail(wish.getThumbnail())
                .build();
    }
}
