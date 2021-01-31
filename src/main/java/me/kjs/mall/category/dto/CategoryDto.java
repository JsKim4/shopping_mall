package me.kjs.mall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.category.Category;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.CollectionTextUtil;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private CommonStatus status;
    private List<CategoryDto> childCategories;

    public static CategoryDto categoryToDto(Category category) {
        if (category.isAvailable() == false) {
            return null;
        }
        List<CategoryDto> list = new ArrayList<>();
        if (CollectionTextUtil.isNotBlank(category.getCategories())) {
            for (Category categoryCategory : category.getCategories()) {

                CategoryDto categoryDto = categoryToDto(categoryCategory);
                if (categoryDto != null) {
                    list.add(categoryDto);
                }
            }
        }
        return CategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .status(category.getStatus())
                .childCategories(CollectionTextUtil.isBlank(list) ? null : list)
                .build();
    }

    public static CategoryDto allProductCategory() {
        return CategoryDto.builder()
                .categoryName("전체상품")
                .categoryId(-1L)
                .status(CommonStatus.USED)
                .build();
    }
}