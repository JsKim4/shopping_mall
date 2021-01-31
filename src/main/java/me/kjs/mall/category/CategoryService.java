package me.kjs.mall.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.category.exception.NotAvailableCategoryParentException;
import me.kjs.mall.category.exception.ParentCategoryNotTopNodeException;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.AvailableUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategoriesFetch() {
        List<Category> categories = categoryRepository.findAllByParentCategory(null);
        categories = AvailableUtil.isAvailableFilter(categories);
        return categories.stream().map(Category::loading).collect(Collectors.toList());
    }

    @Transactional
    public Category createCategory(CategorySaveDto categorySaveDto) {

        Category parentCategory = findParentCategoryById(categorySaveDto.getParentCategoryId());
        if (parentCategory != null && parentCategory.isTopNode() == false) {
            throw new ParentCategoryNotTopNodeException();
        }
        Category category = Category.createCategoryByCreateDto(categorySaveDto, parentCategory);

        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(CategorySaveDto categorySaveDto, Long categoryId) {
        Category category = findCategoryById(categoryId);
        Category parentCategory = findParentCategoryById(categorySaveDto.getParentCategoryId());
        if (category == parentCategory || (parentCategory != null && parentCategory.getParentCategory() == category)) {
            throw new NotAvailableCategoryParentException();
        }
        if (parentCategory != null && parentCategory.isTopNode() == false) {
            throw new ParentCategoryNotTopNodeException();
        }
        category.update(categorySaveDto, parentCategory);
    }

    private Category findParentCategoryById(Long parentCategoryId) {
        if (parentCategoryId == null) {
            return null;
        } else {
            return findCategoryById(parentCategoryId);
        }
    }

    @Transactional
    public void useCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.updateStatus(CommonStatus.USED);
    }


    @Transactional
    public void unUseCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void removeCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.updateStatus(CommonStatus.DELETED);
    }


    private Category findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(category);
        return category;
    }

}
