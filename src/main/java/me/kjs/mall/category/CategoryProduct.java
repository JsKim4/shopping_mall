package me.kjs.mall.category;


import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.product.base.BaseProduct;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_product_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private BaseProduct baseProduct;

    public static CategoryProduct createCategoryProduct(BaseProduct baseProduct, Category category) {
        return CategoryProduct.builder()
                .baseProduct(baseProduct)
                .category(category)
                .build();
    }

    public boolean isEqualsCategory(Category category) {
        return this.category == category;
    }

    public void remove() {
        category = null;
        baseProduct = null;
    }
}
