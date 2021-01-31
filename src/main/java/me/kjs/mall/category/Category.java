package me.kjs.mall.category;


import lombok.*;
import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.CollectionTextUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static me.kjs.mall.common.util.CollectionTextUtil.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @Enumerated(EnumType.STRING)
    private YnType deletable;
    @Enumerated(EnumType.STRING)
    private YnType productContainable;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    @OneToMany(mappedBy = "parentCategory")
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<CategoryProduct> productCategories = new ArrayList<>();

    public static Category createCategoryByCreateDto(CategorySaveDto categorySaveDto, Category parentCategory) {
        return Category.builder()
                .name(categorySaveDto.getName())
                .parentCategory(parentCategory)
                .status(CommonStatus.CREATED)
                .deletable(YnType.Y)
                .productContainable(categorySaveDto.getProductContainable())
                .build();
    }

    public int getChildSize() {
        return categories == null ? 0 : categories.size();
    }

    public boolean isProductContainable() {
        return productContainable == YnType.Y;
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public Category loading() {
        for (Category category : categories) {
            category.loading();
        }
        return this;
    }

    public void updateStatus(CommonStatus status) {
        this.status = status;
    }

    public void update(CategorySaveDto categorySaveDto, Category parentCategory) {
        this.parentCategory = parentCategory;
        if (isNotBlank(categorySaveDto.getName())) {
            name = categorySaveDto.getName();
        }
        if (categorySaveDto.getProductContainable() != null) {
            productContainable = categorySaveDto.getProductContainable();
        }
    }

    public void neverDeleted() {
        deletable = YnType.N;
    }

    public boolean isEndNode() {
        return CollectionTextUtil.isBlank(categories);
    }

    public boolean isTopNode() {
        return parentCategory == null;
    }
}
