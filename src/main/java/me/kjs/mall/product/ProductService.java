package me.kjs.mall.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.BaseProductRepository;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductStockModifyDto;
import me.kjs.mall.product.dto.ProductUpdateDto;
import me.kjs.mall.product.exception.AlreadyExistProductByBaseProductException;
import me.kjs.mall.product.exception.DiscountAmountBiggerThenOriginPriceException;
import me.kjs.mall.product.exception.NotUsedBaseProductException;
import me.kjs.mall.product.type.DiscountType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductQueryRepository productQueryRepository;
    private final BaseProductRepository baseProductRepository;

    @Transactional
    public Product createProduct(ProductCreateDto productCreateDto) {
        BaseProduct baseProduct = findBaseProductById(productCreateDto.getBaseProductId());

        boolean existsByBaseProduct = productQueryRepository.existsByBaseProduct(baseProduct);
        if (existsByBaseProduct) {
            throw new AlreadyExistProductByBaseProductException();
        }

        baseProduct.loading();

        if (baseProduct.isUsed() == false) {
            throw new NotUsedBaseProductException();
        }

        Product product = Product.createProduct(baseProduct, productCreateDto);
        Product saveProduct = productRepository.save(product);
        return saveProduct;
    }

    @Transactional
    public void updateProduct(ProductUpdateDto productUpdateDto, Long productId) {
        Product product = findProductById(productId);
        if (productUpdateDto.getDiscountType() == DiscountType.FLAT_RATE) {
            if (productUpdateDto.getDiscountAmount() > product.getOriginPrice()) {
                throw new DiscountAmountBiggerThenOriginPriceException();
            }
        }
        product.updateProduct(productUpdateDto);
    }

    @Transactional
    public void modifyProductStock(Long productId, ProductStockModifyDto productStockModifyDto) {
        Product product = findProductById(productId);
        int stock = productStockModifyDto.getModifierStock() - product.getStock();
        product.stockModify(ProductStockModifyDto.adminModifyStock(stock));
    }

    @Transactional
    public void removeProduct(Long productId) {
        Product product = findProductById(productId);
        product.remove();
    }

    private BaseProduct findBaseProductById(Long baseProductId) {
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(baseProduct);
        return baseProduct;
    }

    private Product findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(product);
        return product;
    }

    @Transactional
    public void useProduct(Long productId) {
        Product product = findProductById(productId);
        product.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseProduct(Long productId) {
        Product product = findProductById(productId);
        product.updateStatus(CommonStatus.UN_USED);
    }

    public Product findByIdFetch(Long productId) {
        return findProductById(productId).loading();
    }
}