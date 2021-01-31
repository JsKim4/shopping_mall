package me.kjs.mall.product.base;

import lombok.*;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryProduct;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.base.dto.ImageAndOrder;
import me.kjs.mall.product.base.dto.ProvisionNoticeSaveDto;
import me.kjs.mall.product.type.ProductDelivery;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static me.kjs.mall.product.base.dto.ImageAndOrder.createImageAndOrder;


@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseProduct extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "base_product_id")
    private Long id;
    private String code;
    private String name;

    private int originPrice;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<ImageAndOrder> thumbnailImage = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_delivery_id")
    private ProductDelivery productDelivery;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> productTags = new ArrayList<>();

    @Column(length = 4000)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "baseProduct")
    @Builder.Default
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "provision_notice_id")
    private ProvisionNotice provisionNotice;

    public static BaseProduct createBaseProduct(BaseProductSaveDto baseProductSaveDto) {
        ProductDelivery productDelivery = ProductDelivery.createProductDelivery(baseProductSaveDto.getProductDelivery());

        BaseProduct baseProduct = BaseProduct.builder()
                .originPrice(baseProductSaveDto.getOriginPrice())
                .productDelivery(productDelivery)
                .status(CommonStatus.CREATED)
                .productTags(baseProductSaveDto.getTags())
                .code(baseProductSaveDto.getCode())
                .name(baseProductSaveDto.getName())
                .content(baseProductSaveDto.getContents())
                .thumbnailImage(imagesToImageAndOrders(baseProductSaveDto.getThumbnail()))
                .provisionNotice(ProvisionNotice.initialize())
                .build();

        return baseProduct;
    }

    public void addCategory(Category category) {
        boolean isContainsCategory = categoryProducts.stream().anyMatch(c -> c.isEqualsCategory(category));
        if (isContainsCategory == false) {
            categoryProducts.add(CategoryProduct.createCategoryProduct(this, category));
        }
    }

    public void removeCategory(Category category) {
        List<CategoryProduct> categoryProducts = this.categoryProducts.stream().filter(c -> c.isEqualsCategory(category)).collect(Collectors.toList());
        for (CategoryProduct categoryProduct : categoryProducts) {
            this.categoryProducts.remove(categoryProduct);
            categoryProduct.remove();
        }
    }

    public void update(BaseProductSaveDto baseProductSaveDto) {
        this.code = baseProductSaveDto.getCode();
        if (!CollectionTextUtil.isBlank(baseProductSaveDto.getContents())) {
            this.content = baseProductSaveDto.getContents();
        }

        if (!CollectionTextUtil.isBlank(baseProductSaveDto.getThumbnail())) {
            this.thumbnailImage = imagesToImageAndOrders(baseProductSaveDto.getThumbnail());
        }

        this.name = baseProductSaveDto.getName();
        this.productDelivery.update(baseProductSaveDto.getProductDelivery());
        if (!CollectionTextUtil.isBlank(baseProductSaveDto.getTags())) {
            this.productTags = baseProductSaveDto.getTags();
        } else {
            this.productTags = new ArrayList<>();
        }
        if (baseProductSaveDto.getOriginPrice() != 0) {
            this.originPrice = baseProductSaveDto.getOriginPrice();
        }
    }


    private static List<ImageAndOrder> imagesToImageAndOrders(List<String> images) {
        List<ImageAndOrder> newImages = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            newImages.add(createImageAndOrder(images.get(i), i));
        }
        return newImages;
    }

    public List<String> getThumbnailImageUrls() {
        return thumbnailImage
                .stream()
                .sorted((o1, o2) -> o1.getSortOrder() - o2.getSortOrder())
                .map(ImageAndOrder::getImage)
                .collect(Collectors.toList());
    }

    public void updateStatus(CommonStatus commonStatus) {
        status = commonStatus;
    }

    @Override
    public boolean isAvailable() {
        if (status != CommonStatus.DELETED)
            return true;
        return false;
    }

    public void delete() {
        status = CommonStatus.DELETED;
        code = UUID.randomUUID().toString();
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public BaseProduct loading() {
        getProductTags().stream().findFirst();
        getProductDelivery().getFee();
        getThumbnailImageUrls();
        provisionNotice.getCreatedBy();
        return this;
    }

    public int calculateDeliveryFee(int sumPrice) {
        return productDelivery.calculateDeliveryFee(sumPrice);
    }

    public void updateProvision(ProvisionNoticeSaveDto provisionNotice) {
        this.provisionNotice.update(provisionNotice);
    }
}
