package me.kjs.mall.api.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryService;
import me.kjs.mall.category.dto.CategoryDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.member.type.AccountRole.BASE_PRODUCT;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
public class CommonCategoryApiController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseDto queryAllCategories(@CurrentMember Member member) {
        List<Category> categories = categoryService.findAllCategoriesFetch();

        if (!hasRole(member, BASE_PRODUCT)) {
            categories = categories.stream().filter(Category::isUsed).collect(Collectors.toList());
        }

        List<CategoryDto> body = categories.stream().sorted((o1, o2) -> o1.getChildSize() - o2.getChildSize()).map(CategoryDto::categoryToDto).collect(Collectors.toList());

        body.add(CategoryDto.allProductCategory());
        return ResponseDto.ok(body);
    }
}