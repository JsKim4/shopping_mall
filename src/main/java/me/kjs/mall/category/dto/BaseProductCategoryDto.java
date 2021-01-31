package me.kjs.mall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.category.Category;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductCategoryDto {
    private Long categoryId;
    private String categoryName;
    private YnType isContain;
    private CommonStatus status;

    public static BaseProductCategoryDto createBaseProductCategory(Category category, YnType yn) {
        return BaseProductCategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .isContain(yn)
                .status(category.getStatus())
                .build();
    }
}
