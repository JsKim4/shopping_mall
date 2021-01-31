package me.kjs.mall.product;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductStockModifyDto;
import me.kjs.mall.product.dto.ProductUpdateDto;
import me.kjs.mall.product.type.DiscountPolicy;
import me.kjs.mall.product.type.DiscountType;
import me.kjs.mall.product.type.Evaluation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_product_id")
    private BaseProduct baseProduct;
    @Embedded
    private Evaluation evaluation;
    private int stock;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    @NotNull
    private LocalDateTime salesBeginDate;
    @NotNull
    private LocalDateTime salesEndDate;
    @Embedded
    private DiscountPolicy discountPolicy;

    public static Product createProduct(BaseProduct baseProduct, ProductCreateDto productCreateDto) {
        return Product.builder()
                .baseProduct(baseProduct)
                .salesBeginDate(productCreateDto.getSalesBeginDate())
                .salesEndDate(productCreateDto.getSalesEndDate())
                .status(CommonStatus.CREATED)
                .stock(productCreateDto.getStock())
                .discountPolicy(DiscountPolicy.createDiscountPolicy(productCreateDto))
                .evaluation(Evaluation.initializeEvaluation())
                .build();
    }

    public int getOriginPrice() {
        return baseProduct.getOriginPrice();
    }

    public int getPrice() {
        return this.getOriginPrice() - this.getDiscountPrice();
    }

    public DiscountType getDiscountType() {
        return discountPolicy.getDiscountType();
    }

    public int getDiscountPrice() {
        return discountPolicy.getDiscountPrice(baseProduct.getOriginPrice());
    }

    public int getDiscountPercent() {
        return discountPolicy.getDiscountPercent(baseProduct.getOriginPrice());
    }

    public void updateProduct(ProductUpdateDto productUpdateDto) {
        if (productUpdateDto.getSalesBeginDate() != null) {
            salesBeginDate = productUpdateDto.getSalesBeginDate();
        }
        if (productUpdateDto.getSalesEndDate() != null) {
            salesEndDate = productUpdateDto.getSalesEndDate();
        }
        discountPolicy.updateProduct(productUpdateDto);
    }

    public String getBaseProductName() {
        return baseProduct.getName();
    }

    public List<String> getBaseProductThumbnailImage() {
        return baseProduct.getThumbnailImageUrls();
    }

    public void stockModify(ProductStockModifyDto productStockModifyDto) {
        stock += productStockModifyDto.getModifierStock();
    }

    public boolean isStockMoreThen(int modifierStock) {
        return stock >= modifierStock;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public void remove() {
        status = CommonStatus.DELETED;
    }


    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    public Product loading() {
        baseProduct.loading();
        discountPolicy.loading();
        return this;
    }

    public void updateStatus(CommonStatus used) {
        status = used;
    }

    public void delete() {
        status = CommonStatus.DELETED;
    }

    public int calculateDeliveryFee(int sumPrice) {
        return baseProduct.calculateDeliveryFee(sumPrice);
    }

    public void addReview(int score) {
        evaluation.addScore(score);
    }

    public void addQna() {
        evaluation.addQna();
    }

    public void deleteQna() {
        evaluation.deleteQna();
    }

    public String getProductCode() {
        return baseProduct.getCode();
    }
}