package me.kjs.mall.product;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.product.type.DiscountType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ProductServiceTest extends BaseTest {

    @Test
    void queryTest() {

        BaseProduct baseProduct = generatorBaseProductTemp();

        ProductCreateDto productCreateDto = ProductCreateDto.builder()
                .baseProductId(baseProduct.getId())
                .discountAmount(10)
                .discountType(DiscountType.PERCENT)
                .salesBeginDate(LocalDateTime.now())
                .salesEndDate(LocalDateTime.now().plusDays(14))
                .stock(10)
                .build();

        baseProductService.useBaseProduct(baseProduct.getId());

        Product product = productService.createProduct(productCreateDto);


        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();


        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
    }

}