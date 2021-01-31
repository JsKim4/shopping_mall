package me.kjs.mall.order;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.sheet.OrderSheetCreateDto;
import me.kjs.mall.order.sheet.OrderSheetDto;
import me.kjs.mall.order.sheet.OrderSheetProductDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSheetServiceTest extends BaseTest {


    @Test
    @DisplayName("주문서 생성 서비스 성공 케이스 테스트")
    void createOrderSheetTest() {
        List<Product> allProducts = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Product> products = AvailableUtil.isUsedFilter(allProducts);
        List<Product> result = products.stream().collect(Collectors.toList());
        List<ProductIdAndQuantityDto> productIdAndQuantityDtos = result.stream().map(product -> ProductIdAndQuantityDto.builder()
                .productId(product.getId())
                .quantity(2)
                .build()).collect(Collectors.toList());

        OrderSheetCreateDto orderSheetCreateDto = OrderSheetCreateDto.builder()
                .products(productIdAndQuantityDtos)
                .build();

        OrderSheetDto orderSheet = orderSheetService.createOrderSheet(orderSheetCreateDto);

        for (Product product : result) {
            OrderSheetProductDto orderSheetProductDto = orderSheet.getProducts().stream().filter(p -> p.getProductId().equals(product.getId())).findFirst().orElseThrow(NoExistIdException::new);
            assertEquals(orderSheetProductDto.getProductId(), product.getId());
            assertEquals(orderSheetProductDto.getQuantity(), 2);
        }
    }

}