package me.kjs.mall.order.dto;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.sheet.OrderSheetProductDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSheetProductDtoTest extends BaseTest {

    @Test
    @DisplayName("ProductAndQuantity to OrderSheetProductTest")
    void productAndQuantityToOrderSheetProductDtoTest() {
        List<Product> allProducts = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Product> usedProducts = AvailableUtil.isUsedFilter(allProducts);
        int i = 0;
        for (Product product : usedProducts) {
            ProductAndQuantityDto productAndQuantityDto = ProductAndQuantityDto.builder()
                    .product(product)
                    .quantity(++i)
                    .build();
            OrderSheetProductDto orderSheetProductDto = OrderSheetProductDto.productAndQuantityToOrderSheetProductDto(productAndQuantityDto);

            assertEquals(orderSheetProductDto.getProductId(), productAndQuantityDto.getProductId());
            assertEquals(orderSheetProductDto.getQuantity(), productAndQuantityDto.getQuantity());
            assertEquals(orderSheetProductDto.getRequestQuantity(), productAndQuantityDto.getRequestQuantity());
            assertEquals(orderSheetProductDto.getThumbnail(), productAndQuantityDto.getThumbnail());
            assertEquals(orderSheetProductDto.getName(), productAndQuantityDto.getName());
            assertEquals(orderSheetProductDto.getDiscountPercent(), productAndQuantityDto.getDiscountPercent());
            assertEquals(orderSheetProductDto.getOriginPrice(), productAndQuantityDto.getOriginPrice());
            assertEquals(orderSheetProductDto.getPrice(), productAndQuantityDto.getPrice());
            assertEquals(orderSheetProductDto.getDiscountPrice(), productAndQuantityDto.getDiscountPrice());
            assertEquals(orderSheetProductDto.getSumOriginPrice(), productAndQuantityDto.getSumOriginPrice());
            assertEquals(orderSheetProductDto.getSumPrice(), productAndQuantityDto.getSumPrice());
            assertEquals(orderSheetProductDto.getSumDiscountPrice(), productAndQuantityDto.getSumDiscountPrice());
            assertEquals(orderSheetProductDto.getStock(), productAndQuantityDto.getStock());
        }
    }

}