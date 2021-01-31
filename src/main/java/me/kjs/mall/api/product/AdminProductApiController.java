package me.kjs.mall.api.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductService;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductDetailDto;
import me.kjs.mall.product.dto.ProductStockModifyDto;
import me.kjs.mall.product.dto.ProductUpdateDto;
import me.kjs.mall.product.type.DiscountType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
@PreAuthorize("hasRole('ROLE_PRODUCT')")
public class AdminProductApiController {

    private final ProductService productService;

    @PostMapping
    public ResponseDto createProduct(@RequestBody @Validated ProductCreateDto productCreateDto, Errors errors) {
        hasErrorsThrow(errors);
        validation(productCreateDto, errors);
        hasErrorsThrow(errors);

        Product product = productService.createProduct(productCreateDto);

        ProductDetailDto productDetailDto = ProductDetailDto.productToDetailDto(product);

        return ResponseDto.created(productDetailDto);
    }


    @PutMapping("/{productId}")
    public ResponseDto updateProduct(@RequestBody @Validated ProductUpdateDto productUpdateDto, @PathVariable("productId") Long productId, Errors errors) {
        hasErrorsThrow(errors);
        validation(productUpdateDto, errors);
        hasErrorsThrow(errors);
        productService.updateProduct(productUpdateDto, productId);
        return ResponseDto.noContent();
    }

    @DeleteMapping("/{productId}")
    public ResponseDto removeProduct(@PathVariable("productId") Long productId) {
        productService.removeProduct(productId);
        return ResponseDto.noContent();
    }

    @PutMapping("/stock/{productId}")
    public ResponseDto modifyStock(@RequestBody @Validated ProductStockModifyDto productStockModifyDto,
                                   @PathVariable("productId") Long productId,
                                   Errors errors) {
        hasErrorsThrow(errors);
        productService.modifyProductStock(productId, productStockModifyDto);
        return ResponseDto.noContent();
    }

    @PutMapping("/used/{productId}")
    public ResponseDto useProduct(@PathVariable("productId") Long productId) {
        productService.useProduct(productId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{productId}")
    public ResponseDto unUseProduct(@PathVariable("productId") Long productId) {
        productService.unUseProduct(productId);
        return ResponseDto.noContent();
    }


    private void validation(ProductUpdateDto productUpdateDto, Errors errors) {
        if (productUpdateDto.getSalesBeginDate().isAfter(productUpdateDto.getSalesEndDate())) {
            errors.rejectValue("salesBeginDate", "wrong value", "salesBeginDate must before salesEndDate");
        }
        if (productUpdateDto.getDiscountType() != null) {
            if (productUpdateDto.getDiscountType() == DiscountType.PERCENT) {
                if (productUpdateDto.getDiscountAmount() < 0 || productUpdateDto.getDiscountAmount() > 100) {
                    errors.rejectValue("discountAmount", "wrong value", "if discountType equal 'PERCENT' then discountAmount must more then 0 and less then 100");
                }
            }
            if (productUpdateDto.getDiscountType() == DiscountType.NONE) {
                if (productUpdateDto.getDiscountAmount() != 0) {
                    errors.rejectValue("discountAmount", "wrong value", "if discountType 'NONE' then discountAmount must 0");
                }
            } else {
                if (productUpdateDto.getDiscountAmount() == 0) {
                    errors.rejectValue("discountAmount", "wrong value", "if discountType not null then discountAmount must more then 0");
                }
            }
        }

    }

    private void validation(ProductCreateDto productCreateDto, Errors errors) {

        if (productCreateDto.getSalesBeginDate().isAfter(productCreateDto.getSalesEndDate())) {
            errors.rejectValue("salesBeginDate", "wrong value", "salesBeginDate must before salesEndDate");
        }

        if (productCreateDto.getDiscountType() == DiscountType.PERCENT) {
            if (productCreateDto.getDiscountAmount() < 0 || productCreateDto.getDiscountAmount() > 100) {
                errors.rejectValue("discountAmount", "wrong value", "if discountType equal 'PERCENT' then discountAmount must more then 0 and less then 100");
            }
        }

    }


}
