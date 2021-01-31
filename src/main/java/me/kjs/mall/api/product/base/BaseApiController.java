package me.kjs.mall.api.product.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.dto.BaseProductCategoryDto;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ValidationErrorException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.common.util.CompareUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.BaseProductQueryRepository;
import me.kjs.mall.product.base.BaseProductService;
import me.kjs.mall.product.base.dto.BaseProductDetailDto;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.base.dto.BaseProductSearchCondition;
import me.kjs.mall.product.base.dto.ProvisionNoticeSaveDto;
import me.kjs.mall.product.dto.ProductDeliveryDto;
import me.kjs.mall.product.type.DeliveryType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;
import static me.kjs.mall.product.base.dto.BaseProductDetailDto.baseProductToDetailDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products/base")
public class BaseApiController {

    private final BaseProductService baseProductService;
    private final BaseProductQueryRepository baseProductQueryRepository;
    private final CategoryRepository categoryRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto createBaseProduct(@RequestBody @Validated BaseProductSaveDto baseProductSaveDto, Errors errors) {

        hasErrorsThrow(errors);
        if (CollectionTextUtil.isBlank(baseProductSaveDto.getThumbnail())) {
            errors.rejectValue("thumbnail", "wrong value", "thumbnail can't empty");
            throw new ValidationErrorException(errors);
        }
        validation(baseProductSaveDto, errors);
        hasErrorsThrow(errors);

        BaseProduct result = baseProductService.createBaseProduct(baseProductSaveDto);

        return ResponseDto.created(baseProductToDetailDto(result));
    }

    @PutMapping("/{baseProductId}")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto updateBaseProduct(@PathVariable("baseProductId") Long baseProductId,
                                         @RequestBody @Validated BaseProductSaveDto baseProductSaveDto, Errors errors) {
        hasErrorsThrow(errors);
        validation(baseProductSaveDto, errors);
        hasErrorsThrow(errors);

        baseProductService.updateBaseProduct(baseProductId, baseProductSaveDto);

        return ResponseDto.noContent();
    }


    @PutMapping("/provision/{baseProductId}")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto updateBaseProductProvision(@PathVariable("baseProductId") Long baseProductId,
                                                  @RequestBody @Validated ProvisionNoticeSaveDto provisionNotice, Errors errors) {
        hasErrorsThrow(errors);

        baseProductService.updateProductProvision(baseProductId, provisionNotice);

        return ResponseDto.noContent();
    }

    @PutMapping("/used/{baseProductId}")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto usedBaseProduct(@PathVariable("baseProductId") Long baseProductId) {
        baseProductService.useBaseProduct(baseProductId);

        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{baseProductId}")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto unUsedBaseProduct(@PathVariable("baseProductId") Long baseProductId) {
        baseProductService.unUseBaseProduct(baseProductId);

        return ResponseDto.noContent();
    }

    @DeleteMapping("/{baseProductId}")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto deleteBaseProduct(@PathVariable("baseProductId") Long baseProductId) {
        baseProductService.deleteBaseProduct(baseProductId);

        return ResponseDto.noContent();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT') or hasRole('ROLE_PRODUCT')")
    public ResponseDto findBaseProducts(@Validated BaseProductSearchCondition baseProductSearchCondition,
                                        @CurrentMember Member member,
                                        Errors errors) {
        hasErrorsThrow(errors);
        if (baseProductSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "status can't DELETED");
        }
        if (!hasRole(member, AccountRole.BASE_PRODUCT)) {
            if (baseProductSearchCondition.getStatus() != CommonStatus.USED) {
                errors.rejectValue("status", "wrong value", "normal admin status will be only USED");
            }
        }

        CommonPage<BaseProduct> result = baseProductQueryRepository.findBaseProductBySearchCondition(baseProductSearchCondition);
        CommonPage<BaseProduct> body = result.updateContent(result.getContents().stream().map(BaseProductDetailDto::baseProductToDetailDto).collect(Collectors.toList()));

        return ResponseDto.ok(body);
    }

    @PutMapping("/{baseProductId}/categories/add")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto addBaseProductsCategory(@PathVariable("baseProductId") Long baseProductId, @RequestBody OnlyIdsDto onlyIdsDto) {

        baseProductService.addCategory(baseProductId, onlyIdsDto.getIds());

        return ResponseDto.noContent();
    }

    @PutMapping("/{baseProductId}/categories/remove")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto deleteBaseProductsCategory(@PathVariable("baseProductId") Long baseProductId, @RequestBody OnlyIdsDto onlyIdsDto) {

        baseProductService.removeCategory(baseProductId, onlyIdsDto.getIds());

        return ResponseDto.noContent();
    }

    @GetMapping("/{baseProductId}/categories")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto queryBaseProductsCategory(@PathVariable("baseProductId") Long baseProductId) {


        List<Category> existCategory = baseProductService.findCategoryByBaseProduct(baseProductId);
        List<Category> allCategory = AvailableUtil.
                isAvailableFilter(categoryRepository.findAll().stream().filter(c -> c.isProductContainable())
                        .collect(Collectors.toList()));

        List<BaseProductCategoryDto> body = createYnCategories(existCategory, allCategory);

        return ResponseDto.ok(body);
    }

    private List<BaseProductCategoryDto> createYnCategories(List<Category> existCategory, List<Category> allCategory) {
        List<BaseProductCategoryDto> baseProductCategoryDtos = new ArrayList<>(allCategory.size());
        for (Category category : allCategory) {
            if (existCategory.stream().anyMatch(c -> CompareUtil.equals(category.getId(), c.getId()))) {
                baseProductCategoryDtos.add(BaseProductCategoryDto.createBaseProductCategory(category, YnType.Y));
            } else {
                baseProductCategoryDtos.add(BaseProductCategoryDto.createBaseProductCategory(category, YnType.N));
            }
        }
        return baseProductCategoryDtos;
    }

    private void validation(BaseProductSaveDto baseProductSaveDto, Errors errors) {
        ProductDeliveryDto productDelivery = baseProductSaveDto.getProductDelivery();
        if (productDelivery.getDeliveryType() == null) {
            errors.rejectValue("productDelivery.deliveryType", "wring value", "deliveryType can't null");
        }
        if (productDelivery.getDeliveryType() == DeliveryType.FREE) {
            if (productDelivery.getFee() != 0) {
                errors.rejectValue("productDelivery.fee", "wrong value", "fee is only 0 if Type FREE");
            }
            if (productDelivery.getFeeCondition() != 0) {
                errors.rejectValue("productDelivery.feeCondition", "wrong value", "feeCondition is 0 if type CONDITION");
            }
        } else if (productDelivery.getDeliveryType() == DeliveryType.CONDITION) {
            if (productDelivery.getFee() == 0) {
                errors.rejectValue("productDelivery.fee", "wrong value", "fee isn't 0 if type CONDITION");
            }
            if (productDelivery.getFeeCondition() == 0) {
                errors.rejectValue("productDelivery.feeCondition", "wrong value", "feeCondition isn't 0 if type CONDITION");
            }
        }
    }

}
