package me.kjs.mall.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.cart.Cart;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long cartId;
    private Long productId;
    private int quantity;
    private List<String> thumbnail;
    private String name;
    private int discountPercent;
    private int originPrice;
    private int price;
    private int discountPrice;
    private int stock;

    public static CartDto cartToDto(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .thumbnail(cart.getThumbnail())
                .name(cart.getName())
                .discountPercent(cart.getDiscountPercent())
                .discountPrice(cart.getDiscountPrice())
                .price(cart.getPrice())
                .originPrice(cart.getOriginPrice())
                .stock(cart.getStock())
                .build();
    }

}
