package me.kjs.mall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.category.Category;
import me.kjs.mall.common.type.CommonStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailDto {
    private Long categoryId;
    private String name;
    private CommonStatus status;

    public static CategoryDetailDto categoryToDetailDto(Category category) {
        return CategoryDetailDto.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .status(category.getStatus())
                .build();

    }
}
