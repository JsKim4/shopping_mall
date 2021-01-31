package me.kjs.mall.api.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductQueryRepository;
import me.kjs.mall.product.ProductService;
import me.kjs.mall.product.dto.ProductDetailDto;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.wish.WishQueryRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;
import static me.kjs.mall.common.util.ThrowUtil.notUsedThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class CommonProductApiController {

    private final ProductService productService;

    private final WishQueryRepository wishQueryRepository;

    private final ProductQueryRepository productQueryRepository;

    @GetMapping
    public ResponseDto findProducts(@CurrentMember Member member,
                                    @Validated ProductSearchCondition productSearchCondition,
                                    Errors errors) {
        hasErrorsThrow(errors);
        if (productSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "status can't DELETED");
        }
        if (!hasRole(member, AccountRole.PRODUCT)) {
            if (productSearchCondition.getStatus() != CommonStatus.USED) {
                errors.rejectValue("status", "wrong value", "status will be USED only");
            }
        }
        hasErrorsThrow(errors);

        CommonPage<Product> result = productQueryRepository.findProductsBySearchCondition(productSearchCondition);

        CommonPage body = result.updateContent(result.getContents().stream().map(ProductDetailDto::productToDetailDto).collect(Collectors.toList()));

        return ResponseDto.ok(body);
    }

    @GetMapping("/{productId}")
    public ResponseDto findProductsById(@CurrentMember Member member,
                                        @PathVariable("productId") Long productId) {


        Product product = productService.findByIdFetch(productId);
        if (!hasRole(member, AccountRole.PRODUCT)) {
            notUsedThrow(product);
        }
        YnType isWish = YnType.N;
        if (member != null) {
            isWish = wishQueryRepository.existByMemberIdAndProductId(member.getId(), productId) ? YnType.Y : YnType.N;
        } else {
            isWish = YnType.N;
        }

        ProductDetailDto body = ProductDetailDto.productToDetailDto(product, isWish);
        return ResponseDto.ok(body);
    }
}
