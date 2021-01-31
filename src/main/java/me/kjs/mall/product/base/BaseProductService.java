package me.kjs.mall.product.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryProduct;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.base.dto.ProvisionNoticeSaveDto;
import me.kjs.mall.product.exception.AlreadyExistProductCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.isAvailableFilter;
import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BaseProductService {
    private final BaseProductRepository baseProductRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public BaseProduct createBaseProduct(BaseProductSaveDto baseProductSaveDto) {

        if (baseProductRepository.existsByCode(baseProductSaveDto.getCode())) {
            throw new AlreadyExistProductCodeException();
        }

        BaseProduct baseProduct = BaseProduct.createBaseProduct(baseProductSaveDto);

        BaseProduct saveBaseProduct = baseProductRepository.save(baseProduct);

        return saveBaseProduct;
    }

    @Transactional
    public void updateBaseProduct(Long baseProductId, BaseProductSaveDto baseProductSaveDto) {
        BaseProduct baseProduct = findBaseProductById(baseProductId);
        if (baseProduct.getCode().equals(baseProductSaveDto.getCode()) == false) {
            if (baseProductRepository.existsByCode(baseProductSaveDto.getCode())) {
                throw new AlreadyExistProductCodeException();
            }
        }
        baseProduct.update(baseProductSaveDto);
    }

    @Transactional
    public void useBaseProduct(Long baseProductId) {
        BaseProduct baseProduct = findBaseProductById(baseProductId);
        baseProduct.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseBaseProduct(Long baseProductId) {
        BaseProduct baseProduct = findBaseProductById(baseProductId);
        baseProduct.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void deleteBaseProduct(Long baseProductId) {
        BaseProduct baseProduct = findBaseProductById(baseProductId);
        baseProduct.delete();
    }

    private BaseProduct findBaseProductById(Long baseProductId) {
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(baseProduct);
        return baseProduct;
    }

    public List<BaseProduct> findAllFetch() {
        List<BaseProduct> baseProducts = baseProductRepository.findAll();
        baseProducts = isAvailableFilter(baseProducts);
        List<BaseProduct> result = baseProducts.stream().map(BaseProduct::loading).collect(Collectors.toList());
        return result;
    }

    @Transactional
    public void addCategory(Long baseProductId, List<Long> categoriesIds) {
        List<Category> categories = AvailableUtil.isUsedFilter(categoryRepository.findAllById(categoriesIds));
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        for (Category category : categories) {
            if (category.isProductContainable())
                baseProduct.addCategory(category);
        }
    }

    @Transactional
    public void removeCategory(Long baseProductId, List<Long> categoriesIds) {
        List<Category> categories = categoryRepository.findAllById(categoriesIds);
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        for (Category category : categories) {
            baseProduct.removeCategory(category);
        }
    }


    public List<Category> findCategoryByBaseProduct(Long baseProductId) {
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        return AvailableUtil.isAvailableFilter(baseProduct.getCategoryProducts().stream().map(CategoryProduct::getCategory).map(Category::loading).collect(Collectors.toList()));
    }

    @Transactional
    public void updateProductProvision(Long baseProductId, ProvisionNoticeSaveDto provisionNotice) {
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        baseProduct.updateProvision(provisionNotice);
    }
}
