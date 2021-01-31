package me.kjs.mall.order.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.Product;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndQuantityDto {
    private Product product;
    private int quantity;

    public Long getProductId() {
        return product.getId();
    }

    public List<String> getThumbnail() {
        return product.getBaseProductThumbnailImage();
    }

    public String getName() {
        return product.getBaseProductName();
    }

    public int getDiscountPercent() {
        return product.getDiscountPercent();
    }

    public int getOriginPrice() {
        return product.getOriginPrice();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public int getDiscountPrice() {
        return product.getDiscountPrice();
    }

    public int getSumOriginPrice() {
        return product.getOriginPrice() * getQuantity();
    }

    public int getSumPrice() {
        return product.getPrice() * getQuantity();
    }

    public int getSumDiscountPrice() {
        return product.getDiscountPrice() * getQuantity();
    }


    public int getStock() {
        return product.getStock();
    }


    public int getQuantity() {
        return Math.min(quantity, product.getStock());
    }

    public Product getProduct() {
        return product;
    }

    public int calculateDeliveryFee(int sumPrice) {
        return product.calculateDeliveryFee(sumPrice);
    }

    public int getRequestQuantity() {
        return quantity;
    }
}
