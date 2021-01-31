package me.kjs.mall.category;

import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.category.exception.NotAvailableCategoryParentException;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest extends BaseTest {


    @Test
    @DisplayName("부모 카테고리가 없는 카테고리 생성")
    void createCategoryNoParent() {
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("테스트 카테고리")
                .build();
        Category category = categoryService.createCategory(categorySaveDto);

        assertTrue(category.getCategories().isEmpty());
        assertNotNull(category.getId());
        assertEquals(category.getName(), categorySaveDto.getName());
        assertNull(category.getParentCategory());
        assertEquals(category.getStatus(), CommonStatus.CREATED);
    }

    @Test
    @DisplayName("부모 카테고리가 존재하는 카테고리 생성")
    void createCategoryYesParent() {
        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("부모 카테고리")
                .build();
        Category parentCategory = categoryService.createCategory(categorySaveDto);
        CategorySaveDto categorySaveDtoChild = CategorySaveDto.builder()
                .name("자식 카테고리")
                .parentCategoryId(parentCategory.getId())
                .build();

        Category childCategory = categoryService.createCategory(categorySaveDtoChild);
        assertTrue(childCategory.getCategories().isEmpty());
        assertNotNull(childCategory.getId());
        assertEquals(childCategory.getName(), categorySaveDtoChild.getName());
        assertEquals(childCategory.getParentCategory(), parentCategory);
        assertEquals(childCategory.getStatus(), CommonStatus.CREATED);
    }

    @Test
    @DisplayName("부모 카테고리를 지정하였으나 존재하지 않아서 실패하는 케이스")
    void createCategoryYesParentFailNoExistParent() {
        CategorySaveDto categorySaveDtoChild = CategorySaveDto.builder()
                .name("부모 카테고리")
                .parentCategoryId(-1L)
                .build();

        NoExistIdException noExistIdException = assertThrows(NoExistIdException.class, () -> {
            categoryService.createCategory(categorySaveDtoChild);
        });
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() {
        CategorySaveDto categorySaveDtoChild = CategorySaveDto.builder()
                .name("자식 카테고리")
                .build();
        Category childCategory = categoryService.createCategory(categorySaveDtoChild);
        CategorySaveDto categorySaveDtoParent = CategorySaveDto.builder()
                .name("부모 카테고리")
                .build();
        Category parentCategory = categoryService.createCategory(categorySaveDtoParent);
        assertNull(childCategory.getParentCategory());


        CategorySaveDto updateCategoryDto = CategorySaveDto.builder()
                .parentCategoryId(parentCategory.getId())
                .name("modify name")
                .build();
        categoryService.updateCategory(updateCategoryDto, childCategory.getId());
        assertEquals(childCategory.getParentCategory(), parentCategory);
        assertEquals(childCategory.getName(), updateCategoryDto.getName());
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategoryFailNotAvailableParent() {
        CategorySaveDto categorySaveDtoChild = CategorySaveDto.builder()
                .name("자식 카테고리")
                .build();
        Category childCategory = categoryService.createCategory(categorySaveDtoChild);
        CategorySaveDto categorySaveDtoParent = CategorySaveDto.builder()
                .name("부모 카테고리")
                .parentCategoryId(childCategory.getId())
                .build();
        Category parentCategory = categoryService.createCategory(categorySaveDtoParent);
        assertNull(childCategory.getParentCategory());


        CategorySaveDto updateCategoryDto = CategorySaveDto.builder()
                .parentCategoryId(parentCategory.getId())
                .name("modify name")
                .build();
        NotAvailableCategoryParentException notAvailableCategoryParentException = assertThrows(NotAvailableCategoryParentException.class, () -> {
            categoryService.updateCategory(updateCategoryDto, childCategory.getId());
        });
        assertEquals(notAvailableCategoryParentException.getStatus(), 4019);
        assertEquals(notAvailableCategoryParentException.getMessage(), "카테고리의 부모는 자신이 될 수 없습니다. 카테고리의 부모가 서로 크로싱 될수 없습니다.");
    }
}