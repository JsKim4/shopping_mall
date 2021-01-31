package me.kjs.mall.api.category;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.dto.sign.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCategoryApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/categories 카테고리 생성 성공 케이스")
    void createCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategoriesFetch();

        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Parents Category")
                .productContainable(YnType.Y)
                .parentCategoryId(categories.get(0).getId())
                .build();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/categories")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(categorySaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.categoryId").exists())
                .andExpect(jsonPath("data.name").value(categorySaveDto.getName()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andDo(document("create-category",
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름"),
                                fieldWithPath("parentCategoryId").description("상위 카테고리 고유 번호").optional(),
                                fieldWithPath("productContainable").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "상품 포함 가능 여부"))
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("data.name").description("카테고리 이름"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS))
                        )));
    }


    @Test
    @DisplayName("/api/categories/{categoryId} 카테고리 수정 성공 케이스")
    void updateCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategoriesFetch();

        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Parents Category")
                .parentCategoryId(categories.get(0).getId())
                .productContainable(YnType.N)
                .build();

        Category category = categoryService.createCategory(categorySaveDto);

        TokenDto tokenDto = getTokenDto();

        CategorySaveDto categoryUpdateDto = CategorySaveDto.builder()
                .name("modify Category")
                .parentCategoryId(categories.get(1).getId())
                .productContainable(YnType.Y)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{categoryId}", category.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(categoryUpdateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-category",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름").optional(),
                                fieldWithPath("parentCategoryId").description("상위 카테고리 고유 번호 / null 일겨우 null로 초기화"),
                                fieldWithPath("productContainable").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "상품 포함 가능 여부"))
                        )));


        assertEquals(category.getName(), categoryUpdateDto.getName());
        assertEquals(category.getParentCategory().getId(), categoryUpdateDto.getParentCategoryId());
    }

    @Test
    @DisplayName("/api/categories/used/{categoryId} 카테고리 사용 성공 케이스")
    void useCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategoriesFetch();

        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Parents Category")
                .parentCategoryId(categories.get(0).getId())
                .build();

        Category category = categoryService.createCategory(categorySaveDto);

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/used/{categoryId}", category.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-category-used",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        )
                ));


        assertEquals(category.getStatus(), CommonStatus.USED);
    }

    @Test
    @DisplayName("/api/categories/unused/{categoryId} 카테고리 사용 중지 성공 케이스")
    void unUseCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategoriesFetch();

        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Parents Category")
                .parentCategoryId(categories.get(0).getId())
                .build();

        Category category = categoryService.createCategory(categorySaveDto);

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/unused/{categoryId}", category.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-category-unused",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        )
                ));


        assertEquals(category.getStatus(), CommonStatus.UN_USED);
    }

    @Test
    @DisplayName("/api/categories/{categoryId} 카테고리 삭제 성공 케이스")
    void deleteCategory() throws Exception {

        List<Category> categories = categoryService.findAllCategoriesFetch();

        CategorySaveDto categorySaveDto = CategorySaveDto.builder()
                .name("Parents Category")
                .parentCategoryId(categories.get(0).getId())
                .build();

        Category category = categoryService.createCategory(categorySaveDto);

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{categoryId}", category.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("delete-category",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        )
                ));


        assertEquals(category.getStatus(), CommonStatus.DELETED);
    }
}