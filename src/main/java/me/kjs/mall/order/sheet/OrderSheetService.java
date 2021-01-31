package me.kjs.mall.order.sheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CompareUtil;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.isUsedFilter;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderSheetService {

    private final ProductRepository productRepository;

    public OrderSheetDto createOrderSheet(OrderSheetCreateDto orderSheetCreateDto) {
        List<Long> productIds = orderSheetCreateDto.getProducts().stream().map(ProductIdAndQuantityDto::getProductId).collect(Collectors.toList());
        List<Product> allProducts = productRepository.findAllById(productIds);
        List<Product> products = isUsedFilter(allProducts);
        List<ProductAndQuantityDto> productAndQuantityDtoList = productsToProductAndQuantityList(products, orderSheetCreateDto.getProducts());
        return OrderSheetDto.createOrderSheetDto(productAndQuantityDtoList);
    }

    private List<ProductAndQuantityDto> productsToProductAndQuantityList(List<Product> products, List<ProductIdAndQuantityDto> productIdAndQuantityDtos) {
        List<ProductAndQuantityDto> productAndQuantities = new ArrayList<>();
        for (Product product : products) {
            ProductIdAndQuantityDto productIdAndQuantityDto = productIdAndQuantityDtos.stream()
                    .filter(productId -> CompareUtil.equals(productId.getProductId(), product.getId()))
                    .findFirst().orElseThrow(NoExistIdException::new);
            productAndQuantities.add(new ProductAndQuantityDto(product, productIdAndQuantityDto.getQuantity()));
        }
        return productAndQuantities;
    }

}
