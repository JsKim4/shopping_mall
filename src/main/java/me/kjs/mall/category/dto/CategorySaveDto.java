package me.kjs.mall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySaveDto {
    private Long parentCategoryId;
    private String name;
    private YnType productContainable;
}
