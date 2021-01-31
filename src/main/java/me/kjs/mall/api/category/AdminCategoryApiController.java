package me.kjs.mall.api.category;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryService;
import me.kjs.mall.category.dto.CategoryDetailDto;
import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.common.dto.ResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static me.kjs.mall.common.util.CollectionTextUtil.isBlank;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
@PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
public class AdminCategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseDto createCategory(@RequestBody @Validated CategorySaveDto categorySaveDto, Errors errors) {
        hasErrorsThrow(errors);
        if (isBlank(categorySaveDto.getName())) {
            errors.rejectValue("name", "wrong value", "name can't empty");
        }
        if (categorySaveDto.getProductContainable() == null) {
            errors.rejectValue("productContainable", "wrong value", "productContainable can't null");
        }

        Category category = categoryService.createCategory(categorySaveDto);
        CategoryDetailDto body = CategoryDetailDto.categoryToDetailDto(category);
        return ResponseDto.created(body);
    }

    @PutMapping("/{categoryId}")
    public ResponseDto updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody @Validated CategorySaveDto categorySaveDto, Errors errors) {
        hasErrorsThrow(errors);
        categoryService.updateCategory(categorySaveDto, categoryId);
        return ResponseDto.noContent();
    }

    @PutMapping("/used/{categoryId}")
    public ResponseDto useCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.useCategory(categoryId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{categoryId}")
    public ResponseDto unusedCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.unUseCategory(categoryId);
        return ResponseDto.noContent();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseDto removeCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.removeCategory(categoryId);
        return ResponseDto.noContent();
    }

}
